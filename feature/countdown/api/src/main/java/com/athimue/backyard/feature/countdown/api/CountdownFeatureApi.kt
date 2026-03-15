package com.athimue.backyard.feature.countdown.api

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

/**
 * Public contract for the countdown feature.
 * The app module depends only on this interface; the implementation lives in
 * :feature:countdown:impl:ui and is provided by Hilt at runtime.
 */
interface CountdownFeatureApi {
    fun NavGraphBuilder.registerGraph(
        navController: NavController,
        onRaceStarted: () -> Unit,
        onOpenSettings: () -> Unit,
    )
}
