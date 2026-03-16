package com.athimue.backyard.feature.settings.api

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface SettingsFeatureApi {
    fun NavGraphBuilder.registerGraph(
        navController: NavController,
        onBack: () -> Unit,
        onRaceReset: () -> Unit,
    )
}
