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
        "Killian" -> R.drawable.kilian
        "Brice" -> R.drawable.brice
        "Theo" -> R.drawable.theo
        "Hugo" -> R.drawable.hugo
        "Tristan" -> R.drawable.tristan
        "Marc" -> R.drawable.marc
        "Corentin" -> R.drawable.corentin
        "Louis" -> R.drawable.louis
        "Thibaut" -> R.drawable.thibaut
        "Luca" -> R.drawable.luca
        "Aubin" -> R.drawable.aubin
        "Mathieu" -> R.drawable.mathieu
        "Quentin" -> R.drawable.quentin
        else -> R.drawable.mathieu
    },
)
