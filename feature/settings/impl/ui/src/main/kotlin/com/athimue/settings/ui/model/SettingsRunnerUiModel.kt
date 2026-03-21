package com.athimue.settings.ui.model

import androidx.compose.runtime.Immutable
import com.athimue.backyard.feature.race.impl.domain.model.Runner

@Immutable
data class SettingsRunnerUiModel(
    val dossardId: Int,
    val firstName: String
)

fun Runner.toSettingsUiModel() = SettingsRunnerUiModel(
    dossardId = dossardId,
    firstName = firstName
)
