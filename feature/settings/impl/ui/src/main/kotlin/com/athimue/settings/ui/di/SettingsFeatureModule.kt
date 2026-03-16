package com.athimue.settings.ui.di

import com.athimue.backyard.feature.settings.api.SettingsFeatureApi
import com.athimue.settings.ui.navigation.SettingsNavGraphImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsFeatureModule {

    @Binds
    @Singleton
    abstract fun bindSettingsFeatureApi(impl: SettingsNavGraphImpl): SettingsFeatureApi
}
