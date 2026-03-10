package com.athimue.backyard.repository

import com.athimue.backyard.R
import com.athimue.backyard.data.db.dao.LapResultDao
import com.athimue.backyard.data.db.dao.RunnerDao
import com.athimue.backyard.data.db.entity.LapResultEntity
import com.athimue.backyard.data.db.entity.RunnerEntity
import com.athimue.backyard.model.LapResult
import com.athimue.backyard.model.LapStatus
import com.athimue.backyard.model.Runner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

private val DEFAULT_RUNNERS = listOf(
    RunnerEntity(1, "Brice"),
    RunnerEntity(2, "Thibaud"),
    RunnerEntity(3, "Killian"),
    RunnerEntity(4, "Corentin"),
    RunnerEntity(5, "Aubin"),
    RunnerEntity(6, "Theo"),
    RunnerEntity(7, "Louis"),
    RunnerEntity(8, "Marc"),
    RunnerEntity(9, "Hugo"),
    RunnerEntity(10, "Mathieu"),
    RunnerEntity(11, "Tristan"),
    RunnerEntity(12, "Remi"),
    RunnerEntity(13, "Luca"),
    RunnerEntity(14, "Pote de Marc"),
)

@Singleton
class RoomResultsRepository @Inject constructor(
    private val runnerDao: RunnerDao,
    private val lapResultDao: LapResultDao
) : ResultsRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        scope.launch {
            if (runnerDao.count() == 0) {
                runnerDao.insertAll(DEFAULT_RUNNERS)
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
        runnerDao.insert(RunnerEntity(id = nextId, firstName = firstName))
    }

    override suspend fun removeRunner(runnerId: Int) =
        withContext(Dispatchers.IO) { runnerDao.deleteById(runnerId) }

    override suspend fun clearAllResults() =
        withContext(Dispatchers.IO) { lapResultDao.deleteAll() }

    override suspend fun restoreDefaultRunners() = withContext(Dispatchers.IO) {
        runnerDao.deleteAll()
        lapResultDao.deleteAll()
        runnerDao.insertAll(DEFAULT_RUNNERS)
    }

    private fun RunnerEntity.toDomain() = Runner(
        id = id, firstName = firstName, photoResId = R.drawable.logo
    )

    private fun LapResultEntity.toDomain() = LapResult(
        runnerId = runnerId,
        lapNumber = lapNumber,
        time = time,
        status = runCatching { LapStatus.valueOf(status) }.getOrDefault(LapStatus.COMPLETED)
    )
}
