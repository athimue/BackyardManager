package com.athimue.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.athimue.backyard.feature.race.api.navigation.RaceFeatureApi
import com.athimue.backyard.feature.race.api.navigation.RaceRoutes
import com.athimue.ui.screen.ResultsScreen
import javax.inject.Inject

class ResultsNavGraphImpl @Inject constructor() : RaceFeatureApi {

    override fun NavGraphBuilder.registerGraph(
        navController: NavController,
        onBack: () -> Unit,
        onOpenSettings: () -> Unit,
    ) {
        composable(RaceRoutes.RESULTS) {
            ResultsScreen(
                onBack = onBack,
                onOpenSettings = onOpenSettings,
            )
        }
    }
}
