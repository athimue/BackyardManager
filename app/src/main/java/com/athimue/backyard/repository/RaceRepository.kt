package com.athimue.backyard.repository

import com.athimue.backyard.model.RaceState
import kotlinx.coroutines.flow.Flow

interface RaceRepository {
    fun observeRaceState(): Flow<RaceState>
    fun observeStartHour(): Flow<Int>
    fun observeStartMinute(): Flow<Int>
    /** Computed millis for the scheduled start time (used for countdown display only). */
    suspend fun getRaceStartMillis(): Long
    /** Millis recorded when the race actually started. 0 if not started yet. */
    suspend fun getActualStartMillis(): Long
    suspend fun setActualStartMillis(millis: Long)
    suspend fun setStartTime(hour: Int, minute: Int)
    suspend fun setRaceState(state: RaceState)
}
