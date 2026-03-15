package com.athimue.backyard.core.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.athimue.backyard.core.entity.LapResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LapResultDao {
    @Query("SELECT * FROM lap_results")
    fun observeAll(): Flow<List<LapResultEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(result: LapResultEntity)

    @Query("DELETE FROM lap_results WHERE runner_id = :runnerId AND lap_number = :lapNumber")
    suspend fun delete(runnerId: Int, lapNumber: Int)

    @Query("DELETE FROM lap_results")
    suspend fun deleteAll()
}