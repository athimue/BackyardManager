package com.athimue.backyard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athimue.backyard.model.LapResult
import com.athimue.backyard.model.LapStatus
import com.athimue.backyard.model.ResultsUiState
import com.athimue.backyard.repository.ResultsRepository
import com.athimue.backyard.repository.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _uiState = MutableStateFlow(ResultsUiState())
    val uiState: StateFlow<ResultsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.observeRunners(),
                repository.observeLapResults()
            ) { runners, lapResults ->
                val resultsByRunner: Map<Int, Map<Int, LapResult>> =
                    lapResults.groupBy { it.runnerId }
                        .mapValues { (_, list) -> list.associateBy { it.lapNumber } }
                ResultsUiState(runners = runners, results = resultsByRunner)
            }.stateIn(
                scope = this,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ResultsUiState()
            ).collect { _uiState.value = it }
        }
    }

    /**
     * Click cycle per cell:
     *  empty → COMPLETED (records current lap time) → ELIMINATED → empty
     *  Cross cells (after elimination) are ignored.
     */
    fun onLapClicked(runnerId: Int, lapNumber: Int) {
        if (_uiState.value.isCrossCell(runnerId, lapNumber)) return

        viewModelScope.launch {
            when (_uiState.value.statusFor(runnerId, lapNumber)) {
                null -> {
                    val totalSeconds = timerRepository.observeSeconds().first()
                    val lapTime = formatLapTime(totalSeconds % LAP_DURATION_SECONDS)
                    repository.setLapResult(runnerId, lapNumber, lapTime, LapStatus.COMPLETED)
                }
                LapStatus.COMPLETED -> {
                    val existing = _uiState.value.timeFor(runnerId, lapNumber) ?: ""
                    repository.setLapResult(runnerId, lapNumber, existing, LapStatus.ELIMINATED)
                }
                LapStatus.ELIMINATED -> {
                    repository.removeLapResult(runnerId, lapNumber)
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
