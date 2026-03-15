package com.athimue.backyard.di

import android.content.Context
import androidx.room.Room
import com.athimue.backyard.core.database.BackyardDatabase
import com.athimue.backyard.core.database.dao.LapResultDao
import com.athimue.backyard.core.database.dao.RaceDao
import com.athimue.backyard.core.database.dao.RunnerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BackyardDatabase =
        Room.databaseBuilder(context, BackyardDatabase::class.java, "backyard.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideRunnerDao(db: BackyardDatabase): RunnerDao = db.runnerDao()

    @Provides
    fun provideLapResultDao(db: BackyardDatabase): LapResultDao = db.lapResultDao()

    @Provides
    fun provideRaceDao(db: BackyardDatabase): RaceDao = db.raceDao()
}
