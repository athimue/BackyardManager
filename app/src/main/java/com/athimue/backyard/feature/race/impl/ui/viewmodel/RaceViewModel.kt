package com.athimue.backyard.feature.race.impl.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athimue.backyard.feature.race.impl.domain.model.LapStatus.COMPLETED
import com.athimue.backyard.feature.race.impl.domain.model.LapStatus.ELIMINATED
import com.athimue.backyard.feature.race.impl.domain.repository.ResultsRepository
import com.athimue.backyard.feature.race.impl.domain.repository.TimerRepository
import com.athimue.backyard.feature.race.impl.ui.model.LapResultUiModel
import com.athimue.backyard.feature.race.impl.ui.model.ResultsUiState
import com.athimue.backyard.feature.race.impl.ui.model.toLapResultUiModel
import com.athimue.backyard.feature.race.impl.ui.model.toRunnerUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LAP_DURATION_SECONDS = 3600

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val repository: ResultsRepository,
    private val timerRepository: TimerRepository
) : ViewModel() {

    private val _uiState = combine(
        repository.observeRunners(),
        repository.observeLapResults(),
        timerRepository.observeSeconds()
    ) { runners, lapResults, seconds ->
        val resultsByRunner: Map<Int, Map<Int, LapResultUiModel>> =
            lapResults.map { it.toLapResultUiModel() }.groupBy { it.runnerId }
                .mapValues { (_, list) -> list.associateBy { it.lapNumber } }
        val currentLap = seconds / LAP_DURATION_SECONDS + 1
        val sortedRunners = runners.map { it.toRunnerUiModel() }.sortedWith(
            Comparator { a, b ->
                val timeA = lapResults.find {
                    it.runnerId == a.dossardId && it.lapNumber == currentLap && it.status == COMPLETED
                }?.time
                val timeB = lapResults.find {
                    it.runnerId == b.dossardId && it.lapNumber == currentLap && it.status == COMPLETED
                }?.time
                when {
                    timeA != null && timeB != null -> timeA.compareTo(timeB)
                    timeA != null -> -1
                    timeB != null -> 1
                    else -> a.firstName.compareTo(b.firstName)
                }
            }
        )
        ResultsUiState(
            runners = sortedRunners,
            results = resultsByRunner,
            currentLap = currentLap
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ResultsUiState()
    )

    val uiState: StateFlow<ResultsUiState> = _uiState

    /**
     * Records a COMPLETED lap for the runner at the current lap number.
     * Does nothing if a result already exists for that lap.
     */
    fun onRunnerNameClicked(runnerId: Int) {
        viewModelScope.launch {
            val totalSeconds = timerRepository.observeSeconds().first()
            val currentLap = totalSeconds / LAP_DURATION_SECONDS + 1
            if (_uiState.value.statusFor(runnerId, currentLap) == null) {
                val lapTime = formatLapTime(totalSeconds % LAP_DURATION_SECONDS)
                repository.setLapResult(runnerId, currentLap, lapTime, COMPLETED)
            }
        }
    }

    /**
     * Click cycle per cell:
     *  empty → COMPLETED → ELIMINATED → empty (current lap only)
     *  empty → COMPLETED → ELIMINATED → ELIMINATED (past laps, no deletion)
     *  Cross cells (after elimination) are ignored.
     */
    fun onLapClicked(runnerId: Int, lapNumber: Int) {
        if (_uiState.value.isCrossCell(runnerId, lapNumber)) return

        viewModelScope.launch {
            val totalSeconds = timerRepository.observeSeconds().first()
            val currentLap = totalSeconds / LAP_DURATION_SECONDS + 1

            when (_uiState.value.statusFor(runnerId, lapNumber)) {
                null, ELIMINATED -> return@launch
                COMPLETED -> {
                    if (lapNumber == currentLap) {
                        repository.removeLapResult(runnerId, lapNumber)
                    }
                }
            }
        }
    }

    private fun formatLapTime(totalSeconds: Int): String {
        val minutes = totalSeconds / 60
        val secs = totalSeconds % 60
        return "%02d:%02d".format(minutes, secs)
    }
}
