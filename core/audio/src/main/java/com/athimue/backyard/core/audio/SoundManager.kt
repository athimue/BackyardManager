package com.athimue.backyard.core.audio

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.ToneGenerator
import com.athimue.backyard.core.AUDIO_WARNING_BEEP_COUNT
import com.athimue.backyard.core.AUDIO_WARNING_REPEAT_DELAY_MS
import com.athimue.backyard.core.AUDIO_WARNING_TONE_DURATION_MS
import com.athimue.backyard.core.AUDIO_WARNING_VOLUME
import com.athimue.backyard.core.LAP_FUN_FINAL_SECONDS
import com.athimue.backyard.core.LAP_FUN_VOCAL_MILESTONES
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    /**
     * Pool de vocaux courts aléatoires. Chaque son joué est retiré définitivement
     * du pool pour ne jamais être rejoué durant toute la backyard.
     * Restauré en intégralité via [reset].
     */
    private val fullVocalPool: List<Int> = listOf(
        R.raw.allezplusite,
        R.raw.arrete_la_drogue,
        R.raw.bababooey,
        R.raw.bassem,
        R.raw.bien_evidemment,
        R.raw.cuisine,
        R.raw.deg,
        R.raw.engines,
        R.raw.fermer,
        R.raw.homer_le_zizi,
        R.raw.indiansong,
        R.raw.kaaris,
        R.raw.leboeuf,
        R.raw.lisopaine,
        R.raw.macron,
        R.raw.marine_le_pens,
        R.raw.mlg_airhorn,
        R.raw.movie_1,
        R.raw.nul,
        R.raw.ouais_cest_greg,
        R.raw.ouille,
        R.raw.rehehehe,
        R.raw.sauce_curry,
        R.raw.seeyouagain,
        R.raw.sensibilite,
        R.raw.sncf,
        R.raw.taladalleettaenviedeken,
        R.raw.tk78,
        R.raw.ton_telephone,
        R.raw.totaly_spies,
        R.raw.une_tuileeeeeeee,
        R.raw.verstappen,
        R.raw.yeah_boiii,
        R.raw.zizi_fesse,
    )

    private val vocalPool: MutableList<Int> = fullVocalPool.toMutableList()

    private var funAudioLap: Int = -1
    private var finalStartedThisLap: Boolean = false
    private val triggeredMilestonesThisLap: MutableSet<Int> = mutableSetOf()
    private var lapStartMediaPlayer: MediaPlayer? = null
    private var finalMediaPlayer: MediaPlayer? = null
    private var vocalMediaPlayer: MediaPlayer? = null

    fun playNewLapStart() {
        scope.launch {
            releaseLapStartPlayer()
            lapStartMediaPlayer = MediaPlayer.create(context, R.raw.mario_race_start)?.apply {
                setOnCompletionListener { releaseLapStartPlayer() }
                start()
            }
        }
    }

    fun playEndLapWarning() {
        scope.launch {
            repeat(AUDIO_WARNING_BEEP_COUNT) {
                val tone = ToneGenerator(AudioManager.STREAM_MUSIC, AUDIO_WARNING_VOLUME)
                tone.startTone(ToneGenerator.TONE_PROP_BEEP2, AUDIO_WARNING_TONE_DURATION_MS)
                delay(AUDIO_WARNING_REPEAT_DELAY_MS)
                tone.release()
            }
        }
    }

    /**
     * Appelée à chaque tick (1 s) du timer.
     *
     * - Aux jalons [LAP_FUN_VOCAL_MILESTONES] (10 min / 7 min / 4 min restantes) : joue 1 vocal
     *   aléatoire retiré définitivement du pool.
     * - À [LAP_FUN_FINAL_SECONDS] restantes (30 s) : lance `final_countdown` (une fois par tour).
     */
    fun updateLapFunAudio(remainingSecondsInLap: Int, currentLap: Int) {
        if (currentLap != funAudioLap) {
            funAudioLap = currentLap
            finalStartedThisLap = false
            triggeredMilestonesThisLap.clear()
            releaseFinalPlayer()
            releaseVocalPlayer()
        }

        when {
            remainingSecondsInLap <= LAP_FUN_FINAL_SECONDS -> {
                if (!finalStartedThisLap) {
                    releaseVocalPlayer()
                    startFinalTrack()
                    finalStartedThisLap = true
                }
            }

            else -> {
                val milestone = LAP_FUN_VOCAL_MILESTONES.firstOrNull { milestone ->
                    remainingSecondsInLap <= milestone && milestone !in triggeredMilestonesThisLap
                }
                if (milestone != null) {
                    triggeredMilestonesThisLap.add(milestone)
                    playRandomVocal()
                }
            }
        }
    }

    private fun startFinalTrack() {
        releaseFinalPlayer()
        finalMediaPlayer = MediaPlayer.create(context, R.raw.final_countdown)?.apply {
            setOnCompletionListener { releaseFinalPlayer() }
            start()
        }
    }

    /**
     * Pioche un vocal aléatoire dans [vocalPool] et le retire définitivement.
     * @return false si le pool est vide ou si la création du MediaPlayer échoue.
     */
    private fun playRandomVocal(): Boolean {
        if (vocalPool.isEmpty()) return false
        releaseVocalPlayer()
        val index = vocalPool.indices.random()
        val resId = vocalPool.removeAt(index)
        val player = MediaPlayer.create(context, resId)?.apply {
            setOnCompletionListener { releaseVocalPlayer() }
            start()
        }
        vocalMediaPlayer = player
        return player != null
    }

    private fun releaseFinalPlayer() {
        finalMediaPlayer?.release()
        finalMediaPlayer = null
    }

    private fun releaseVocalPlayer() {
        vocalMediaPlayer?.release()
        vocalMediaPlayer = null
    }

    private fun releaseLapStartPlayer() {
        lapStartMediaPlayer?.release()
        lapStartMediaPlayer = null
    }

    /**
     * Remet le SoundManager à zéro : restore le pool complet, réinitialise tous les états
     * de tour. À appeler lors d'un reset de course.
     */
    fun reset() {
        releaseFinalPlayer()
        releaseVocalPlayer()
        releaseLapStartPlayer()
        vocalPool.clear()
        vocalPool.addAll(fullVocalPool)
        funAudioLap = -1
        finalStartedThisLap = false
        triggeredMilestonesThisLap.clear()
    }
}
