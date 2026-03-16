package com.athimue.backyard.feature.timer.api

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface TimerFeatureApi {
    fun NavGraphBuilder.registerGraph(
        navController: NavController,
        onShowResults: () -> Unit,
        onOpenSettings: () -> Unit,
    )
}
