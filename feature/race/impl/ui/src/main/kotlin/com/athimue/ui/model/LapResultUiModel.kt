package com.athimue.ui.model

import androidx.compose.runtime.Immutable
import com.athimue.backyard.feature.race.impl.domain.model.LapResult
import com.athimue.backyard.feature.race.impl.domain.model.LapStatus

@Immutable
data class LapResultUiModel(
    val runnerId: Int,
    val lapNumber: Int,
    val time: String,
    val status: LapStatusUiModel = LapStatusUiModel.COMPLETED
)

fun LapResult.toLapResultUiModel() = LapResultUiModel(
    runnerId = runnerId,
    lapNumber = lapNumber,
    time = time,
    status = status.toUiModel()
)

private fun LapStatus.toUiModel() = when (this) {
    LapStatus.COMPLETED -> LapStatusUiModel.COMPLETED
    LapStatus.ELIMINATED -> LapStatusUiModel.ELIMINATED
}