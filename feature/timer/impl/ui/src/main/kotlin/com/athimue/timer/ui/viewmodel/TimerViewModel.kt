package com.athimue.timer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athimue.backyard.core.audio.SoundManager
import com.athimue.backyard.feature.race.impl.domain.model.LapStatus
import com.athimue.timer.ui.model.TimerUiState
import com.athimue.timer.ui.model.formatCurrentTime
import com.athimue.backyard.feature.race.impl.domain.repository.RaceRepository
import com.athimue.backyard.feature.race.impl.domain.repository.ResultsRepository
import com.athimue.backyard.feature.race.impl.domain.repository.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LAP_DURATION_SECONDS = 3600

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerRepository: TimerRepository,
    private val raceRepository: RaceRepository,
    private val resultsRepository: ResultsRepository,
    private val soundManager: SoundManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    private var previousLap = 0
    private var warningPlayedForLap = 0

    init {
        viewModelScope.launch { observeActiveRunnersCount() }
        startTimer()
    }

    private suspend fun observeActiveRunnersCount() {
        combine(
            resultsRepository.observeRunners(),
            resultsRepository.observeLapResults()
        ) { runners, lapResults ->
            runners.count { runner ->
                lapResults.none { it.runnerId == runner.dossardId && it.status == LapStatus.ELIMINATED }
            }
        }.collect { active ->
            _uiState.update { it.copy(activeRunnersCount = active) }
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            // Seed previousLap from actual elapsed time to avoid false triggers on restart
            val seedStartMillis = raceRepository.getActualStartMillis()
            val seedElapsed = if (seedStartMillis > 0L)
                ((System.currentTimeMillis() - seedStartMillis) / 1000).toInt().coerceAtLeast(0)
            else 0
            previousLap = seedElapsed / LAP_DURATION_SECONDS + 1
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
                if (currentLap > previousLap) {
                    autoEliminateRunners(previousLap)
                    soundManager.playNewLapStart()
                    triggerLapFlash()
                    previousLap = currentLap
                }

                // End-of-lap warning (once per lap, at 30s remaining)
                if (remaining == 30 && warningPlayedForLap != currentLap) {
                    soundManager.playEndLapWarning()
                    warningPlayedForLap = currentLap
                }

                _uiState.update { it.copy(seconds = elapsed, currentTime = formatCurrentTime()) }
                timerRepository.updateSeconds(elapsed)
                delay(1_000)
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
