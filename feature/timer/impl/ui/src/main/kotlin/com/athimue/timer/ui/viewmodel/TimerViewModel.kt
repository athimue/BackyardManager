package com.athimue.timer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athimue.backyard.core.LAP_DURATION_SECONDS
import com.athimue.backyard.core.LAP_WARNING_SECONDS
import com.athimue.backyard.core.audio.SoundManager
import com.athimue.backyard.feature.race.api.model.RaceState
import com.athimue.backyard.feature.race.impl.domain.model.LapStatus
import com.athimue.timer.ui.model.TimerUiState
import com.athimue.timer.ui.model.formatCurrentTime
import com.athimue.backyard.feature.race.impl.domain.repository.RaceRepository
import com.athimue.backyard.feature.race.impl.domain.repository.ResultsRepository
import com.athimue.backyard.feature.race.impl.domain.repository.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerRepository: TimerRepository,
    private val raceRepository: RaceRepository,
    private val resultsRepository: ResultsRepository,
    private val soundManager: SoundManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    private val _finishEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val finishEvent: SharedFlow<Unit> = _finishEvent.asSharedFlow()

    private var previousLap = 0
    private var warningPlayedForLap = 0
    private var hasSeenActiveRunners = false
    private var finishEmitted = false

    init {
        viewModelScope.launch { observeActiveRunnersCount() }
        startTimer()
    }

    private suspend fun observeActiveRunnersCount() {
        combine(
            resultsRepository.observeRunners(),
            resultsRepository.observeLapResults()
        ) { runners, lapResults ->
            val eliminationLap: Map<Int, Int> = lapResults
                .filter { it.status == LapStatus.ELIMINATED }
                .groupBy { it.runnerId }
                .mapValues { (_, list) -> list.minOf { it.lapNumber } }
            val activeRunners = runners.filter { it.dossardId !in eliminationLap }

            // Backyard rule: the last runner standing wins only once they complete a loop
            // that every other runner failed to complete.
            val winnerFound = runners.size > 1 && activeRunners.size == 1 && run {
                val last = activeRunners.single()
                val othersOutAt = eliminationLap.values.maxOrNull() ?: return@run false
                lapResults.any {
                    it.runnerId == last.dossardId &&
                        it.status == LapStatus.COMPLETED &&
                        it.lapNumber >= othersOutAt
                }
            }
            RunnersSnapshot(
                total = runners.size,
                active = activeRunners.size,
                lastRunnerName = activeRunners.singleOrNull()?.takeIf { runners.size > 1 }?.firstName,
                winnerFound = winnerFound,
            )
        }.collect { snapshot ->
            if (snapshot.active > 0) hasSeenActiveRunners = true
            val everybodyOut = snapshot.total > 0 && snapshot.active == 0 && hasSeenActiveRunners
            if ((everybodyOut || snapshot.winnerFound) && !finishEmitted) {
                finishEmitted = true
                _finishEvent.tryEmit(Unit)
                viewModelScope.launch { raceRepository.setRaceState(RaceState.FINISHED) }
            }
            _uiState.update {
                it.copy(activeRunnersCount = snapshot.active, lastRunnerName = snapshot.lastRunnerName)
            }
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            // Seed previousLap from actual elapsed time to avoid false triggers on restart
            val seedStartMillis = raceRepository.getActualStartMillis()
            val seedElapsed = if (seedStartMillis > 0L)
                ((System.currentTimeMillis() - seedStartMillis) / 1000).toInt().coerceAtLeast(0)
            else 0
            val seedLap = seedElapsed / LAP_DURATION_SECONDS + 1

            // Catch-up after a restart (app killed, TV standby, crash…): every lap that ended
            // while the app was not running must still be processed. Silent: no sound, no flash.
            // Idempotent — runners that already have a result (or were eliminated earlier) are skipped.
            // Not after the race is over (the winner must not be DNF'd on the next lap).
            if (raceRepository.observeRaceState().first() == RaceState.FINISHED) finishEmitted = true
            if (!finishEmitted) {
                for (missedLap in 1 until seedLap) {
                    autoEliminateRunners(missedLap)
                }
            }

            previousLap = seedLap
            warningPlayedForLap = previousLap

            // Cache the actual start millis — it won't change once the race is running.
            var cachedStartMillis = seedStartMillis

            while (isActive) {
                // Re-read only if not yet set (race could have just started)
                if (cachedStartMillis == 0L) {
                    cachedStartMillis = raceRepository.getActualStartMillis()
                }
                val elapsed = if (cachedStartMillis > 0L)
                    ((System.currentTimeMillis() - cachedStartMillis) / 1000).toInt().coerceAtLeast(0)
                else 0

                val currentLap = elapsed / LAP_DURATION_SECONDS + 1
                val remaining = LAP_DURATION_SECONDS - (elapsed % LAP_DURATION_SECONDS)

                // New lap detected
                if (currentLap > previousLap && !finishEmitted) {
                    // Usually a single lap, but covers ticks delayed by more than a lap (e.g. device sleep).
                    for (endedLap in previousLap until currentLap) {
                        autoEliminateRunners(endedLap)
                    }
                    soundManager.playNewLapStart()
                    triggerLapFlash()
                    previousLap = currentLap
                }

                // End-of-lap warning (once per lap, at 30s remaining)
                if (remaining <= LAP_WARNING_SECONDS && warningPlayedForLap != currentLap && !finishEmitted) {
                    soundManager.playEndLapWarning()
                    warningPlayedForLap = currentLap
                }

                if (!finishEmitted) {
                    soundManager.updateLapFunAudio(remainingSecondsInLap = remaining, currentLap = currentLap)
                }

                _uiState.update { it.copy(seconds = elapsed, currentTime = formatCurrentTime()) }
                timerRepository.updateSeconds(elapsed)
                // Wake up just after the next elapsed-second boundary so the display
                // never skips or freezes a second (a fixed 1 s delay slowly drifts in phase).
                val phase = if (cachedStartMillis > 0L)
                    (System.currentTimeMillis() - cachedStartMillis).mod(1_000L)
                else 0L
                delay(1_000L - phase + TICK_MARGIN_MS)
            }
        }
    }

    private fun triggerLapFlash() {
        viewModelScope.launch {
            _uiState.update { it.copy(lapJustChanged = true) }
            delay(2_000)
            _uiState.update { it.copy(lapJustChanged = false) }
        }
    }

    private suspend fun autoEliminateRunners(completedLap: Int) {
        val runners = resultsRepository.observeRunners().first()
        val results = resultsRepository.observeLapResults().first()

        runners.forEach { runner ->
            val alreadyHasResult = results.any {
                it.runnerId == runner.dossardId && it.lapNumber == completedLap
            }
            val alreadyEliminated = results.any {
                it.runnerId == runner.dossardId && it.status == LapStatus.ELIMINATED && it.lapNumber < completedLap
            }
            if (!alreadyHasResult && !alreadyEliminated) {
                resultsRepository.setLapResult(runner.dossardId, completedLap, "DNF", LapStatus.ELIMINATED)
            }
        }
    }
}

/** Small margin so the tick lands after the second boundary, not right before it. */
private const val TICK_MARGIN_MS = 10L

private data class RunnersSnapshot(
    val total: Int,
    val active: Int,
    val lastRunnerName: String?,
    val winnerFound: Boolean,
)
