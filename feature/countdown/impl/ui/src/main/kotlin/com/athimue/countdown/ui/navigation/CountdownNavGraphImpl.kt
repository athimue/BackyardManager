package com.athimue.countdown.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.athimue.backyard.feature.countdown.api.CountdownFeatureApi
import com.athimue.backyard.feature.countdown.api.CountdownRoutes
import com.athimue.backyard.feature.countdown.impl.ui.screen.CountdownScreen
import javax.inject.Inject

class CountdownNavGraphImpl @Inject constructor() : CountdownFeatureApi {

    override fun NavGraphBuilder.registerGraph(
        navController: NavController,
        onRaceStarted: () -> Unit,
        onOpenSettings: () -> Unit,
    ) {
        composable(CountdownRoutes.COUNTDOWN) {
            CountdownScreen(
                onRaceStarted = onRaceStarted,
                onOpenSettings = onOpenSettings,
            )
        }
    }
}
