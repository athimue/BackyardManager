package com.athimue.backyard.feature.countdown.impl.ui.model

import androidx.compose.runtime.Immutable
import com.athimue.backyard.feature.countdown.api.model.RaceState

@Immutable
enum class RaceStateUiModel {
    COUNTDOWN,
    IN_PROGRESS,
    FINISHED
}

fun RaceState.toRaceStateUiModel(): RaceStateUiModel {
    return when (this) {
        RaceState.COUNTDOWN -> RaceStateUiModel.COUNTDOWN
        RaceState.IN_PROGRESS -> RaceStateUiModel.IN_PROGRESS
        RaceState.FINISHED -> RaceStateUiModel.FINISHED
    }
}