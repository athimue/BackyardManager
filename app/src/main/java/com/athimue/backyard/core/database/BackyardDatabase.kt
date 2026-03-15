package com.athimue.backyard.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.athimue.backyard.core.database.dao.LapResultDao
import com.athimue.backyard.core.database.dao.RaceDao
import com.athimue.backyard.core.database.dao.RunnerDao
import com.athimue.backyard.core.database.entity.LapResultEntity
import com.athimue.backyard.core.database.entity.RaceEntity
import com.athimue.backyard.core.database.entity.RunnerEntity

@Database(
    entities = [RunnerEntity::class, LapResultEntity::class, RaceEntity::class],
    version = 2,
    exportSchema = false
)
abstract class BackyardDatabase : RoomDatabase() {
    abstract fun runnerDao(): RunnerDao
    abstract fun lapResultDao(): LapResultDao
    abstract fun raceDao(): RaceDao
}