package com.athimue.backyard.feature.race.impl.domain.model

data class LapResult(
    val runnerId: Int,
    val lapNumber: Int,
    val time: String,
    val status: LapStatus = LapStatus.COMPLETED
)
