package com.athimue.timer.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.athimue.backyard.feature.timer.api.TimerFeatureApi
import com.athimue.backyard.feature.timer.api.TimerRoutes
import com.athimue.timer.ui.screen.TimerScreen
import javax.inject.Inject

class TimerNavGraphImpl @Inject constructor() : TimerFeatureApi {

    override fun NavGraphBuilder.registerGraph(
        navController: NavController,
        onShowResults: () -> Unit,
        onOpenSettings: () -> Unit,
    ) {
        composable(TimerRoutes.TIMER) {
            TimerScreen(
                onShowResults = onShowResults,
                onOpenSettings = onOpenSettings,
            )
        }
    }
}
