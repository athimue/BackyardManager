package com.athimue.ui.di

import com.athimue.backyard.feature.race.api.navigation.RaceFeatureApi
import com.athimue.ui.navigation.ResultsNavGraphImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RaceFeatureModule {

    @Binds
    @Singleton
    abstract fun bindRaceFeatureApi(impl: ResultsNavGraphImpl): RaceFeatureApi
}
