package com.athimue.ui.model

import androidx.compose.runtime.Immutable
import com.athimue.backyard.feature.race.impl.domain.model.LapResult
import com.athimue.backyard.feature.race.impl.domain.model.LapStatus

@Immutable
data class LapResultUiModel(
    val runnerId: Int,
    val lapNumber: Int,
    val time: String,
    val status: LapStatus = LapStatus.COMPLETED
)

fun LapResult.toLapResultUiModel() = LapResultUiModel(
    runnerId = runnerId,
    lapNumber = lapNumber,
    time = time,
    status = status
)