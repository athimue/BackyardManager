package com.athimue.timer.ui.model

import androidx.compose.runtime.Immutable
import com.athimue.backyard.core.LAP_DURATION_SECONDS
import java.util.Calendar

@Immutable
data class TimerUiState(
    val seconds: Int = 0,
    val currentTime: String = formatCurrentTime(),
    val activeRunnersCount: Int = 0,
    val lapJustChanged: Boolean = false
) {
    val currentLap: Int
        get() = (seconds / LAP_DURATION_SECONDS) + 1

    val elapsedSecondsInLap: Int
        get() = seconds % LAP_DURATION_SECONDS

    val remainingSecondsInLap: Int
        get() = LAP_DURATION_SECONDS - elapsedSecondsInLap

    val remainingTimeFormatted: String
        get() = formatTime(remainingSecondsInLap)

    val remainingSecondsFormatted: String
        get() = formatSeconds(remainingSecondsInLap)

    val lapProgress: Float
        get() = elapsedSecondsInLap.toFloat() / LAP_DURATION_SECONDS

    val showEndLapCountdown: Boolean
        get() = remainingSecondsInLap <= 30

    val elapsedRaceFormatted: String
        get() = formatTime(seconds)

    val isLapEndUrgent: Boolean
        get() = remainingSecondsInLap <= 30
}

private fun formatTime(totalSeconds: Int): String {
    val minutes = (totalSeconds % 3600) / 60
    val secs = totalSeconds % 60
    return "%02d:%02d".format(minutes, secs)
}

private fun formatSeconds(totalSeconds: Int): String {
    val secs = totalSeconds % 60
    return "%02d".format(secs, secs)
}

fun formatCurrentTime(): String {
    val c = Calendar.getInstance()
    return "%02d:%02d:%02d".format(
        c.get(Calendar.HOUR_OF_DAY),
        c.get(Calendar.MINUTE),
        c.get(Calendar.SECOND)
    )
}
