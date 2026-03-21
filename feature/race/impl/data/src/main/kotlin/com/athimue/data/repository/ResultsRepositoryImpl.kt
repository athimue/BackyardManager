package com.athimue.data.repository

import com.athimue.backyard.core.database.dao.LapResultDao
import com.athimue.backyard.core.database.dao.RunnerDao
import com.athimue.backyard.core.database.entity.LapResultEntity
import com.athimue.backyard.core.database.entity.RunnerEntity
import com.athimue.backyard.feature.race.impl.domain.model.LapResult
import com.athimue.backyard.feature.race.impl.domain.model.LapStatus
import com.athimue.backyard.feature.race.impl.domain.model.Runner
import com.athimue.backyard.feature.race.impl.domain.repository.ResultsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResultsRepositoryImpl @Inject constructor(
    private val runnerDao: RunnerDao,
    private val lapResultDao: LapResultDao
) : ResultsRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        scope.launch {
            if (runnerDao.count() == 0) {
                runnerDao.insertAll(DefaultRunners.list)
            }
        }
    }

    override fun observeRunners(): Flow<List<Runner>> =
        runnerDao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeLapResults(): Flow<List<LapResult>> =
        lapResultDao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun setLapResult(
        runnerId: Int, lapNumber: Int, time: String, status: LapStatus
    ) = withContext(Dispatchers.IO) {
        lapResultDao.insert(
            LapResultEntity(runnerId = runnerId, lapNumber = lapNumber, time = time, status = status.name)
        )
    }

    override suspend fun removeLapResult(runnerId: Int, lapNumber: Int) =
        withContext(Dispatchers.IO) { lapResultDao.delete(runnerId, lapNumber) }

    override suspend fun addRunner(firstName: String) = withContext(Dispatchers.IO) {
        val nextId = (runnerDao.count()) + 100
        runnerDao.insert(RunnerEntity(dossardId = nextId, firstName = firstName))
    }

    override suspend fun removeRunner(runnerId: Int) =
        withContext(Dispatchers.IO) { runnerDao.deleteById(runnerId) }

    override suspend fun clearAllResults() =
        withContext(Dispatchers.IO) { lapResultDao.deleteAll() }

    override suspend fun restoreDefaultRunners() = withContext(Dispatchers.IO) {
        runnerDao.deleteAll()
        lapResultDao.deleteAll()
        runnerDao.insertAll(DefaultRunners.list)
    }

    private fun RunnerEntity.toDomain() = Runner(
        dossardId = dossardId,
        firstName = firstName,
    )

    private fun LapResultEntity.toDomain() = LapResult(
        runnerId = runnerId,
        lapNumber = lapNumber,
        time = time,
        status = runCatching { LapStatus.valueOf(status) }.getOrDefault(LapStatus.COMPLETED)
    )
}
