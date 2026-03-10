package com.athimue.backyard.di

import com.athimue.backyard.repository.InMemoryTimerRepository
import com.athimue.backyard.repository.RaceRepository
import com.athimue.backyard.repository.ResultsRepository
import com.athimue.backyard.repository.RoomRaceRepository
import com.athimue.backyard.repository.RoomResultsRepository
import com.athimue.backyard.repository.TimerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindResultsRepository(impl: RoomResultsRepository): ResultsRepository

    @Binds
    @Singleton
    abstract fun bindTimerRepository(impl: InMemoryTimerRepository): TimerRepository

    @Binds
    @Singleton
    abstract fun bindRaceRepository(impl: RoomRaceRepository): RaceRepository
}
