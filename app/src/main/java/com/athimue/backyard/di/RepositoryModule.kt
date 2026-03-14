package com.athimue.backyard.di

import com.athimue.backyard.feature.race.impl.data.repository.TimerRepositoryImpl
import com.athimue.backyard.feature.race.impl.domain.repository.RaceRepository
import com.athimue.backyard.feature.race.impl.domain.repository.ResultsRepository
import com.athimue.backyard.feature.race.impl.data.repository.RaceRepositoryImpl
import com.athimue.backyard.feature.race.impl.data.repository.ResultsRepositoryImpl
import com.athimue.backyard.feature.race.impl.domain.repository.TimerRepository
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
    abstract fun bindResultsRepository(impl: ResultsRepositoryImpl): ResultsRepository

    @Binds
    @Singleton
    abstract fun bindTimerRepository(impl: TimerRepositoryImpl): TimerRepository

    @Binds
    @Singleton
    abstract fun bindRaceRepository(impl: RaceRepositoryImpl): RaceRepository
}
