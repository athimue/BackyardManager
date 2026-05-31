package com.athimue.backyard

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.athimue.backyard.feature.countdown.api.CountdownFeatureApi
import com.athimue.backyard.feature.race.api.navigation.RaceFeatureApi
import com.athimue.backyard.feature.settings.api.SettingsFeatureApi
import com.athimue.backyard.feature.timer.api.TimerFeatureApi
import com.athimue.backyard.navigation.BackyardNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var countdownFeatureApi: CountdownFeatureApi

    @Inject
    lateinit var raceFeatureApi: RaceFeatureApi

    @Inject
    lateinit var settingsFeatureApi: SettingsFeatureApi

    @Inject
    lateinit var timerFeatureApi: TimerFeatureApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            BackyardNavHost(
                countdownFeatureApi = countdownFeatureApi,
                raceFeatureApi = raceFeatureApi,
                settingsFeatureApi = settingsFeatureApi,
                timerFeatureApi = timerFeatureApi,
            )
        }
    }
}
