package com.athimue.timer.ui.di

import com.athimue.backyard.feature.timer.api.TimerFeatureApi
import com.athimue.timer.ui.navigation.TimerNavGraphImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TimerFeatureModule {

    @Binds
    @Singleton
    abstract fun bindTimerFeatureApi(impl: TimerNavGraphImpl): TimerFeatureApi
}
