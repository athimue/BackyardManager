package com.athimue.backyard.core.audio

import android.media.AudioManager
import android.media.ToneGenerator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundManager @Inject constructor() {

    private val toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 90)

    /** 3 short beeps — last 30 seconds warning. */
    fun playEndLapWarning() {
        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 1500)
    }

    /** Long ascending beep — new lap started. */
    fun playNewLapStart() {
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 2000)
    }

    fun release() {
        toneGenerator.release()
    }
}
