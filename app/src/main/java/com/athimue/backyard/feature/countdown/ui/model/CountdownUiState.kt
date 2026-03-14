package com.athimue.backyard.feature.countdown.ui.model

import androidx.compose.runtime.Immutable
import com.athimue.backyard.model.RaceState

@Immutable
data class CountdownUiState(
    val raceState: RaceState = RaceState.COUNTDOWN,
    val startHour: Int = 9,
    val startMinute: Int = 0,
    val currentTime: String = "",
    val countdownSeconds: Long = 0L
) {
    val countdownFormatted: String
        get() {
            val h = countdownSeconds / 3600
            val m = (countdownSeconds % 3600) / 60
            val s = countdownSeconds % 60
            return "%02d:%02d:%02d".format(h, m, s)
        }

    val startTimeFormatted: String
        get() = "%02d:%02d".format(startHour, startMinute)
}