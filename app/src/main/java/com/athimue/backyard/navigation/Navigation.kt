package com.athimue.backyard.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.athimue.backyard.feature.countdown.api.CountdownFeatureApi
import com.athimue.backyard.feature.countdown.api.CountdownRoutes
import com.athimue.backyard.feature.race.api.RaceFeatureApi
import com.athimue.backyard.feature.race.api.RaceRoutes
import com.athimue.backyard.feature.settings.ui.screen.SettingsScreen
import com.athimue.backyard.feature.timer.ui.screen.TimerScreen

private const val ROUTE_TIMER = "timer"
private const val ROUTE_SETTINGS = "settings"

@Composable
fun BackyardNavHost(
    countdownFeatureApi: CountdownFeatureApi,
    raceFeatureApi: RaceFeatureApi,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = CountdownRoutes.COUNTDOWN,
        modifier = modifier
    ) {
        with(countdownFeatureApi) {
            registerGraph(
                navController = navController,
                onRaceStarted = {
                    navController.navigate(ROUTE_TIMER) {
                        popUpTo(CountdownRoutes.COUNTDOWN) { inclusive = true }
                    }
                },
                onOpenSettings = { navController.navigate(ROUTE_SETTINGS) },
            )
        }

        composable(ROUTE_TIMER) {
            TimerScreen(
                onShowResults = { navController.navigate(RaceRoutes.RESULTS) },
                onOpenSettings = { navController.navigate(ROUTE_SETTINGS) }
            )
        }

        with(raceFeatureApi) {
            registerGraph(
                navController = navController,
                onBack = { navController.popBackStack() },
                onOpenSettings = { navController.navigate(ROUTE_SETTINGS) },
            )
        }

        composable(ROUTE_SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onRaceReset = {
                    navController.navigate(CountdownRoutes.COUNTDOWN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
