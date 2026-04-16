package com.athimue.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athimue.backyard.feature.race.impl.domain.model.LapStatus
import com.athimue.backyard.feature.race.impl.domain.repository.ResultsRepository
import com.athimue.ui.model.FinishUiState
import com.athimue.ui.model.PodiumEntryUiModel
import com.athimue.ui.model.toRunnerUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FinishViewModel @Inject constructor(
    private val repository: ResultsRepository,
) : ViewModel() {

    val uiState: StateFlow<FinishUiState> = combine(
        repository.observeRunners(),
        repository.observeLapResults()
    ) { runners, lapResults ->
        val ranked = runners
            .map { runner ->
                val runnerResults = lapResults.filter { it.runnerId == runner.dossardId }
                val completedLaps = runnerResults.count { it.status == LapStatus.COMPLETED }
                val eliminatedOnLap = runnerResults
                    .filter { it.status == LapStatus.ELIMINATED }
                    .minByOrNull { it.lapNumber }
                    ?.lapNumber
                PodiumEntryUiModel(
                    rank = 0,
                    runner = runner.toRunnerUiModel(),
                    completedLaps = completedLaps,
                    eliminatedOnLap = eliminatedOnLap,
                )
            }
            .sortedWith(
                compareByDescending<PodiumEntryUiModel> { it.completedLaps }
                    .thenByDescending { it.eliminatedOnLap ?: Int.MAX_VALUE }
            )
            .mapIndexed { index, entry -> entry.copy(rank = index + 1) }

        FinishUiState(
            podium = ranked.take(3),
            others = ranked.drop(3),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FinishUiState(),
    )
}
