package com.athimue.backyard.core.audio

import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

private const val LAP_START_VOLUME = 100
private const val LAP_START_TONE_DURATION_MS = 150
private const val LAP_START_REPEAT_DELAY_MS = 300L
private const val LAP_START_BEEP_COUNT = 3

private const val WARNING_VOLUME = 80
private const val WARNING_TONE_DURATION_MS = 250
private const val WARNING_REPEAT_DELAY_MS = 500L
private const val WARNING_BEEP_COUNT = 2

@Singleton
class SoundManager @Inject constructor() {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun playNewLapStart() {
        scope.launch {
            repeat(LAP_START_BEEP_COUNT) {
                val tone = ToneGenerator(AudioManager.STREAM_MUSIC, LAP_START_VOLUME)
                tone.startTone(ToneGenerator.TONE_PROP_BEEP, LAP_START_TONE_DURATION_MS)
                delay(LAP_START_REPEAT_DELAY_MS)
                tone.release()
            }
        }
    }

    fun playEndLapWarning() {
        scope.launch {
            repeat(WARNING_BEEP_COUNT) {
                val tone = ToneGenerator(AudioManager.STREAM_MUSIC, WARNING_VOLUME)
                tone.startTone(ToneGenerator.TONE_PROP_BEEP2, WARNING_TONE_DURATION_MS)
                delay(WARNING_REPEAT_DELAY_MS)
                tone.release()
            }
        }
    }
}
