package com.athimue.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class FinishUiState(
    val podium: List<PodiumEntryUiModel> = emptyList(),
    val others: List<PodiumEntryUiModel> = emptyList(),
)

@Immutable
data class PodiumEntryUiModel(
    val rank: Int,
    val runner: RunnerUiModel,
    val completedLaps: Int,
    val eliminatedOnLap: Int?,
)
