package com.athimue.backyard.feature.result.ui.model

import androidx.compose.runtime.Immutable

enum class LapStatus { COMPLETED, ELIMINATED }

@Immutable
data class LapResult(
    val runnerId: Int,
    val lapNumber: Int,
    val time: String,
    val status: LapStatus = LapStatus.COMPLETED
)
