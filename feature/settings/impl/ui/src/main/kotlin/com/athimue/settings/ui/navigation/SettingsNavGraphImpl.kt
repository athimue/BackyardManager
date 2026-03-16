package com.athimue.settings.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.athimue.backyard.feature.settings.api.SettingsFeatureApi
import com.athimue.backyard.feature.settings.api.SettingsRoutes
import com.athimue.settings.ui.screen.SettingsScreen
import javax.inject.Inject

class SettingsNavGraphImpl @Inject constructor() : SettingsFeatureApi {

    override fun NavGraphBuilder.registerGraph(
        navController: NavController,
        onBack: () -> Unit,
        onRaceReset: () -> Unit,
    ) {
        composable(SettingsRoutes.SETTINGS) {
            SettingsScreen(
                onBack = onBack,
                onRaceReset = onRaceReset,
            )
        }
    }
}
