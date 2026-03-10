package com.athimue.backyard.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.athimue.backyard.data.db.dao.LapResultDao
import com.athimue.backyard.data.db.dao.RaceDao
import com.athimue.backyard.data.db.dao.RunnerDao
import com.athimue.backyard.data.db.entity.LapResultEntity
import com.athimue.backyard.data.db.entity.RaceEntity
import com.athimue.backyard.data.db.entity.RunnerEntity

@Database(
    entities = [RunnerEntity::class, LapResultEntity::class, RaceEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun runnerDao(): RunnerDao
    abstract fun lapResultDao(): LapResultDao
    abstract fun raceDao(): RaceDao
}
