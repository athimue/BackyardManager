package com.athimue.backyard.core.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.athimue.backyard.core.entity.RaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RaceDao {
    @Query("SELECT * FROM race WHERE id = 1")
    fun observe(): Flow<RaceEntity?>

    @Query("SELECT * FROM race WHERE id = 1")
    suspend fun get(): RaceEntity?

    @Query("SELECT COUNT(*) FROM race")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(race: RaceEntity)

    @Query("UPDATE race SET state = :state WHERE id = 1")
    suspend fun updateState(state: String)

    @Query("UPDATE race SET start_hour = :hour, start_minute = :minute WHERE id = 1")
    suspend fun updateStartTime(hour: Int, minute: Int)

    @Query("UPDATE race SET actual_start_millis = :millis WHERE id = 1")
    suspend fun updateActualStartMillis(millis: Long)

    @Query("SELECT actual_start_millis FROM race WHERE id = 1")
    suspend fun getActualStartMillis(): Long?
}