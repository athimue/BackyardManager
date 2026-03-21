package com.athimue.ui.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.athimue.backyard.feature.race.impl.domain.model.Runner
import com.athimue.backyard.core.theme.R as CoreR

@Immutable
data class RunnerUiModel(
    val dossardId: Int,
    val firstName: String,
    @DrawableRes val photoResId: Int,
)

fun Runner.toRunnerUiModel() = RunnerUiModel(
    dossardId = dossardId,
    firstName = firstName,
    photoResId = CoreR.drawable.logo,
)
