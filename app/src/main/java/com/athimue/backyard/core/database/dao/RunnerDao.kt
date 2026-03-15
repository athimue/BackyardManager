package com.athimue.backyard.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.athimue.backyard.core.database.entity.RunnerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RunnerDao {
    @Query("SELECT * FROM runners ORDER BY dossardId ASC")
    fun observeAll(): Flow<List<RunnerEntity>>

    @Query("SELECT COUNT(*) FROM runners")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(runner: RunnerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(runners: List<RunnerEntity>)

    @Delete
    suspend fun delete(runner: RunnerEntity)

    @Query("DELETE FROM runners WHERE dossardId = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM runners")
    suspend fun deleteAll()
}
