package com.athimue.backyard.feature.countdown.impl.ui.model

import androidx.compose.runtime.Immutable
import com.athimue.backyard.core.SECONDS_PER_MINUTE
import com.athimue.backyard.core.SECONDS_PER_HOUR
import com.athimue.backyard.core.TIME_FORMAT_HH_MM_SS

@Immutable
data class CountdownUiState(
    val raceState: RaceStateUiModel = RaceStateUiModel.COUNTDOWN,
    val startHour: Int = 20,
    val startMinute: Int = 0,
    val currentTime: String = "",
    val countdownSeconds: Long = 0L
) {
    val countdownFormatted: String
        get() {
            val h = countdownSeconds / SECONDS_PER_HOUR
            val m = (countdownSeconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE
            val s = countdownSeconds % SECONDS_PER_MINUTE
            return TIME_FORMAT_HH_MM_SS.format(h, m, s)
        }

    val startTimeFormatted: String
        get() = "%02d:%02d".format(startHour, startMinute)
}