package com.athimue.backyard.feature.race.impl.data.repository

import com.athimue.backyard.core.database.dao.RaceDao
import com.athimue.backyard.core.database.entity.RaceEntity
import com.athimue.backyard.feature.countdown.api.model.RaceState
import com.athimue.backyard.feature.race.impl.domain.repository.RaceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RaceRepositoryImpl @Inject constructor(
    private val raceDao: RaceDao
) : RaceRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        scope.launch {
            if (raceDao.count() == 0) {
                raceDao.insert(RaceEntity())
            }
        }
    }

    override fun observeRaceState(): Flow<RaceState> =
        raceDao.observe().map { entity ->
            entity?.state?.let { runCatching { RaceState.valueOf(it) }.getOrNull() }
                ?: RaceState.COUNTDOWN
        }

    override fun observeStartHour(): Flow<Int> =
        raceDao.observe().map { it?.startHour ?: 20 }

    override fun observeStartMinute(): Flow<Int> =
        raceDao.observe().map { it?.startMinute ?: 0 }

    override suspend fun getRaceStartMillis(): Long = withContext(Dispatchers.IO) {
        val entity = raceDao.get() ?: RaceEntity()
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, entity.startHour)
            set(Calendar.MINUTE, entity.startMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    override suspend fun getActualStartMillis(): Long = withContext(Dispatchers.IO) {
        raceDao.getActualStartMillis() ?: 0L
    }

    override suspend fun setActualStartMillis(millis: Long) = withContext(Dispatchers.IO) {
        raceDao.updateActualStartMillis(millis)
    }

    override suspend fun setStartTime(hour: Int, minute: Int) = withContext(Dispatchers.IO) {
        raceDao.updateStartTime(hour, minute)
    }

    override suspend fun setRaceState(state: RaceState) = withContext(Dispatchers.IO) {
        raceDao.updateState(state.name)
    }
}
