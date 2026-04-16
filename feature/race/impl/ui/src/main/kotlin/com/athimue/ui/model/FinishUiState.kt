package com.athimue.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class PodiumEntryUiModel(
    val rank: Int,
    val runner: RunnerUiModel,
    val completedLaps: Int,
    val eliminatedOnLap: Int?,
)

@Immutable
data class FinishUiState(
    val podium: List<PodiumEntryUiModel> = emptyList(),
    val others: List<PodiumEntryUiModel> = emptyList(),
) {
    val isLoading: Boolean get() = podium.isEmpty() && others.isEmpty()
}
