package com.athimue.ui.model

import androidx.compose.runtime.Immutable
import com.athimue.backyard.feature.race.impl.domain.model.LapStatus

private const val MAX_LAPS = 15

@Immutable
data class ResultsUiState(
    val runners: List<RunnerUiModel> = emptyList(),
    val results: Map<Int, Map<Int, LapResultUiModel>> = emptyMap(),
    val currentLap: Int = 1
) {
    val laps: List<Int>
        get() {
            val highestResultLap = results.values.flatMap { it.keys }.maxOrNull() ?: 0
            val maxLap = maxOf(currentLap + 1, highestResultLap + 1, 5).coerceAtMost(MAX_LAPS)
            return (1..maxLap).toList()
        }

    val activeRunnersCount: Int
        get() = runners.count { eliminationLapFor(it.dossardId) == null }

    val eliminatedRunnersCount: Int
        get() = runners.count { eliminationLapFor(it.dossardId) != null }

    fun lapResultFor(runnerId: Int, lapNumber: Int): LapResultUiModel? =
        results[runnerId]?.get(lapNumber)

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
