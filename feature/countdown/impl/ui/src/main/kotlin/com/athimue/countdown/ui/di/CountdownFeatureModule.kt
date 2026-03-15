package com.athimue.countdown.ui.di

import com.athimue.backyard.feature.countdown.api.CountdownFeatureApi
import com.athimue.countdown.ui.navigation.CountdownNavGraphImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CountdownFeatureModule {

    @Binds
    @Singleton
    abstract fun bindCountdownFeatureApi(impl: CountdownNavGraphImpl): CountdownFeatureApi
}
