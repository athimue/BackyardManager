package com.athimue.backyard.model

import androidx.compose.runtime.Immutable

private const val MAX_LAPS = 15

@Immutable
data class ResultsUiState(
    val runners: List<Runner> = emptyList(),
    val results: Map<Int, Map<Int, LapResult>> = emptyMap()
) {
    val laps: List<Int> = (1..MAX_LAPS).toList()

    val activeRunnersCount: Int
        get() = runners.count { eliminationLapFor(it.id) == null }

    val eliminatedRunnersCount: Int
        get() = runners.count { eliminationLapFor(it.id) != null }

    fun lapResultFor(runnerId: Int, lapNumber: Int): LapResult? =
        results[runnerId]?.get(lapNumber)

    fun timeFor(runnerId: Int, lapNumber: Int): String? =
        lapResultFor(runnerId, lapNumber)?.time

    fun statusFor(runnerId: Int, lapNumber: Int): LapStatus? =
        lapResultFor(runnerId, lapNumber)?.status

    fun eliminationLapFor(runnerId: Int): Int? =
        results[runnerId]
            ?.values
            ?.filter { it.status == LapStatus.ELIMINATED }
            ?.minByOrNull { it.lapNumber }
            ?.lapNumber

    fun isCrossCell(runnerId: Int, lapNumber: Int): Boolean {
        val eliminatedAt = eliminationLapFor(runnerId) ?: return false
        return lapNumber > eliminatedAt
    }

    fun completedLapsFor(runnerId: Int): Int =
        results[runnerId]?.values?.count { it.status == LapStatus.COMPLETED } ?: 0

    fun bestLapTimeFor(runnerId: Int): String? =
        results[runnerId]?.values
            ?.filter { it.status == LapStatus.COMPLETED }
            ?.minByOrNull { it.time }
            ?.time
}
