package com.athimue.backyard.feature.race.impl.ui.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

@Immutable
data class Runner(
    val dossardId: Int,
    val firstName: String,
    @DrawableRes val photoResId: Int
)