package com.athimue.backyard.feature.race.impl.domain.repository

import com.athimue.backyard.feature.race.impl.domain.model.LapResult
import com.athimue.backyard.feature.race.impl.domain.model.LapStatus
import com.athimue.backyard.feature.race.impl.domain.model.Runner
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