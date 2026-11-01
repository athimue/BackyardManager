package com.athimue.backyard.navigation

import androidx.compose.animation.scaleIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.fadeIn
import androidx.compose.animation.core.tween
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
        modifier = modifier,
        // Smooth cross-fade + subtle zoom between screens (TV-friendly, no sliding)
        enterTransition = { fadeIn(tween(350)) + scaleIn(tween(350), initialScale = 0.97f) },
        exitTransition = { fadeOut(tween(250)) },
        popEnterTransition = { fadeIn(tween(350)) + scaleIn(tween(350), initialScale = 1.03f) },
        popExitTransition = { fadeOut(tween(250)) },
    ) {
        with(countdownFeatureApi) {
            registerGraph(
                navController = navController,
                onRaceStarted = {
                    navController.navigate(TimerRoutes.TIMER) {
                        popUpTo(CountdownRoutes.COUNTDOWN) { inclusive = true }
                    }
                },
                onRaceFinished = {
                    // Same back stack as a live finish: Timer → Finish
                    navController.navigate(TimerRoutes.TIMER) {
                        popUpTo(CountdownRoutes.COUNTDOWN) { inclusive = true }
                    }
                    navController.navigate(RaceRoutes.FINISH)
                },
                onOpenSettings = { navController.navigate(SettingsRoutes.SETTINGS) },
            )
        }

        with(timerFeatureApi) {
            registerGraph(
                navController = navController,
                onShowResults = { navController.navigate(RaceRoutes.RESULTS) },
                onOpenSettings = { navController.navigate(SettingsRoutes.SETTINGS) },
                onRaceFinished = { navController.navigate(RaceRoutes.FINISH) },
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
