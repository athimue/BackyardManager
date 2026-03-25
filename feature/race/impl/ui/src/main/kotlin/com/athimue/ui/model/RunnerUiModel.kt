package com.athimue.ui.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.athimue.backyard.feature.race.impl.ui.R
import com.athimue.domain.model.Runner

@Immutable
data class RunnerUiModel(
    val dossardId: Int,
    val firstName: String,
    @DrawableRes val photoResId: Int,
)

fun Runner.toRunnerUiModel() = RunnerUiModel(
    dossardId = dossardId,
    firstName = firstName,
    photoResId = when(firstName) {
        "Aubin" -> R.drawable.aubin
        "Corentin" -> R.drawable.corentin
        "Kilian" -> R.drawable.kilian
        "Louis" -> R.drawable.louis
        "Marc" -> R.drawable.marc
        "Mathieu" -> R.drawable.mathieu
        "Tristan" -> R.drawable.tristan
        else -> R.drawable.mathieu
    },
)
