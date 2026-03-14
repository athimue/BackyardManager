package com.athimue.backyard.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.athimue.backyard.feature.countdown.impl.ui.screen.CountdownScreen
import com.athimue.backyard.feature.race.impl.ui.screen.ResultsScreen
import com.athimue.backyard.feature.settings.ui.screen.SettingsScreen
import com.athimue.backyard.feature.timer.ui.screen.TimerScreen

private const val ROUTE_COUNTDOWN = "countdown"
private const val ROUTE_TIMER = "timer"
private const val ROUTE_RESULTS = "results"
private const val ROUTE_SETTINGS = "settings"

@Composable
fun BackyardNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = ROUTE_COUNTDOWN,
        modifier = modifier
    ) {
        composable(ROUTE_COUNTDOWN) {
            CountdownScreen(
                onRaceStarted = {
                    navController.navigate(ROUTE_TIMER) {
                        popUpTo(ROUTE_COUNTDOWN) { inclusive = true }
                    }
                },
                onOpenSettings = { navController.navigate(ROUTE_SETTINGS) }
            )
        }

        composable(ROUTE_TIMER) {
            TimerScreen(
                onShowResults = { navController.navigate(ROUTE_RESULTS) },
                onOpenSettings = { navController.navigate(ROUTE_SETTINGS) }
            )
        }

        composable(ROUTE_RESULTS) {
            ResultsScreen(
                onBack = { navController.popBackStack() },
                onOpenSettings = { navController.navigate(ROUTE_SETTINGS) }
            )
        }

        composable(ROUTE_SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onRaceReset = {
                    navController.navigate(ROUTE_COUNTDOWN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
