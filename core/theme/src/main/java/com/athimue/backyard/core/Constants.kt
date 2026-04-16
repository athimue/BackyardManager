package com.athimue.backyard.core

/**
 * Durée d'un tour en secondes (3600 = 1 h).
 * Pour tester vite les vocaux / la finale en local, tu peux temporairement mettre **150** (2 min 30).
 */
const val LAP_DURATION_SECONDS = 3600
const val SECONDS_PER_HOUR = 3600L
const val SECONDS_PER_MINUTE = 60L
const val MILLIS_PER_SECOND = 1_000L
const val TIME_FORMAT_HH_MM_SS = "%02d:%02d:%02d"
const val RACE_AUTO_START_GRACE_SECONDS = 300L
const val LAP_WARNING_SECONDS = 30
const val RESULTS_MAX_LAPS = 15

const val AUDIO_WARNING_VOLUME = 80
const val AUDIO_WARNING_TONE_DURATION_MS = 250
const val AUDIO_WARNING_REPEAT_DELAY_MS = 500L
const val AUDIO_WARNING_BEEP_COUNT = 2

/** Dernières secondes du tour : lecture de `final_countdown` (une fois par tour). */
const val LAP_FUN_FINAL_SECONDS = 30

/**
 * Jalons (temps restant en secondes) auxquels un vocal aléatoire est joué durant le tour.
 * = 10 min, 7 min et 4 min avant la fin.
 * Chaque vocal joué est retiré définitivement du pool pour ne plus se répéter.
 */
val LAP_FUN_VOCAL_MILESTONES = listOf(600, 420, 240)

const val EVENT_NAME = "BACKYARD DU GARAGE"
const val EVENT_EDITION = "1ère édition"
const val EVENT_DATE = "17 avril 2026"
const val EVENT_SUBTITLE = "$EVENT_EDITION - $EVENT_DATE"
