package com.athimue.backyard.core.audio

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    // 3 short beeps on new lap start
    fun playNewLapStart() {
        scope.launch {
            repeat(3) {
                val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
                tone.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
                delay(300)
                tone.release()
            }
        }
    }

    // 2 warning beeps at 30s remaining
    fun playEndLapWarning() {
        scope.launch {
            repeat(2) {
                val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
                tone.startTone(ToneGenerator.TONE_PROP_BEEP2, 250)
                delay(500)
                tone.release()
            }
        }
    }
}
