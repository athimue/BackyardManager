package com.athimue.backyard.feature.race.api.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

/**
 * Public contract for the race feature. The app module depends only on this interface.
 * The concrete implementation lives in :feature:race:impl:ui and is provided by Hilt.
 */
interface RaceFeatureApi {
    /**
     * Registers all race-feature destinations into the given [NavGraphBuilder].
     * Navigation callbacks let the feature trigger app-level navigation events.
     */
    fun NavGraphBuilder.registerGraph(
        navController: NavController,
        onBack: () -> Unit,
        onOpenSettings: () -> Unit,
    )
}
