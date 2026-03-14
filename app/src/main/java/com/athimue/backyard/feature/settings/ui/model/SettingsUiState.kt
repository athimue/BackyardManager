package com.athimue.backyard.feature.settings.ui.model

import androidx.compose.runtime.Immutable
import com.athimue.backyard.model.RaceState
import com.athimue.backyard.feature.result.ui.model.Runner

@Immutable
data class SettingsUiState(
    val startHour: Int = 9,
    val startMinute: Int = 0,
    val runners: List<Runner> = emptyList(),
    val raceState: RaceState = RaceState.COUNTDOWN
) {
    val startTimeFormatted: String
        get() = "%02d:%02d".format(startHour, startMinute)
}