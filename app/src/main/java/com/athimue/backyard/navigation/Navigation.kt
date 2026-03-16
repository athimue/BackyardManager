package com.athimue.backyard.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.athimue.backyard.feature.countdown.api.CountdownFeatureApi
import com.athimue.backyard.feature.countdown.api.CountdownRoutes
import com.athimue.backyard.feature.race.api.navigation.RaceFeatureApi
import com.athimue.backyard.feature.race.api.navigation.RaceRoutes
import com.athimue.backyard.feature.settings.api.SettingsFeatureApi
import com.athimue.backyard.feature.settings.api.SettingsRoutes
import com.athimue.backyard.feature.timer.api.TimerFeatureApi
import com.athimue.backyard.feature.timer.api.TimerRoutes

@Composable
fun BackyardNavHost(
    countdownFeatureApi: CountdownFeatureApi,
    raceFeatureApi: RaceFeatureApi,
    settingsFeatureApi: SettingsFeatureApi,
    timerFeatureApi: TimerFeatureApi,
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
                    navController.navigate(TimerRoutes.TIMER) {
                        popUpTo(CountdownRoutes.COUNTDOWN) { inclusive = true }
                    }
                },
                onOpenSettings = { navController.navigate(SettingsRoutes.SETTINGS) },
            )
        }

        with(timerFeatureApi) {
            registerGraph(
                navController = navController,
                onShowResults = { navController.navigate(RaceRoutes.RESULTS) },
                onOpenSettings = { navController.navigate(SettingsRoutes.SETTINGS) },
            )
        }

        with(raceFeatureApi) {
            registerGraph(
                navController = navController,
                onBack = { navController.popBackStack() },
                onOpenSettings = { navController.navigate(SettingsRoutes.SETTINGS) },
            )
        }

        with(settingsFeatureApi) {
            registerGraph(
                navController = navController,
                onBack = { navController.popBackStack() },
                onRaceReset = {
                    navController.navigate(CountdownRoutes.COUNTDOWN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
    }
}
