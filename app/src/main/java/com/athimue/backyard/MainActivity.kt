package com.athimue.backyard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.athimue.backyard.feature.race.api.navigation.RaceFeatureApi
import com.athimue.backyard.navigation.BackyardNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var raceFeatureApi: RaceFeatureApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BackyardNavHost(raceFeatureApi = raceFeatureApi)
        }
    }
}
