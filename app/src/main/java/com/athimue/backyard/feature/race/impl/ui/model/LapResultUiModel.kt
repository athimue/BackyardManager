package com.athimue.backyard.feature.race.impl.ui.model

import androidx.compose.runtime.Immutable
import com.athimue.backyard.feature.race.impl.domain.model.LapResult
import com.athimue.backyard.feature.race.impl.domain.model.LapStatus
import com.athimue.backyard.feature.race.impl.domain.model.Runner

@Immutable
data class LapResultUiModel(
    val runnerId: Int,
    val lapNumber: Int,
    val time: String,
    val status: LapStatus = LapStatus.COMPLETED
)

fun LapResult.LapResultUiModel() = LapResultUiModel(
    runnerId = runnerId,
    lapNumber = lapNumber,
    time = time,
    status = status
)