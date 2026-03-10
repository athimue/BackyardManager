package com.athimue.backyard.di

import android.content.Context
import androidx.room.Room
import com.athimue.backyard.data.db.AppDatabase
import com.athimue.backyard.data.db.dao.LapResultDao
import com.athimue.backyard.data.db.dao.RaceDao
import com.athimue.backyard.data.db.dao.RunnerDao
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "backyard.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideRunnerDao(db: AppDatabase): RunnerDao = db.runnerDao()

    @Provides
    fun provideLapResultDao(db: AppDatabase): LapResultDao = db.lapResultDao()

    @Provides
    fun provideRaceDao(db: AppDatabase): RaceDao = db.raceDao()
}
