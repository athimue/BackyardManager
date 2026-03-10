package com.athimue.backyard.repository

import com.athimue.backyard.model.LapResult
import com.athimue.backyard.model.LapStatus
import com.athimue.backyard.model.Runner
import kotlinx.coroutines.flow.Flow

interface ResultsRepository {
    fun observeRunners(): Flow<List<Runner>>
    fun observeLapResults(): Flow<List<LapResult>>
    suspend fun setLapResult(runnerId: Int, lapNumber: Int, time: String, status: LapStatus)
    suspend fun removeLapResult(runnerId: Int, lapNumber: Int)
    suspend fun addRunner(firstName: String)
    suspend fun removeRunner(runnerId: Int)
    suspend fun clearAllResults()
    suspend fun restoreDefaultRunners()
}
