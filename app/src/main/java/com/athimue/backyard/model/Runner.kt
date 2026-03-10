package com.athimue.backyard.model

import androidx.compose.runtime.Immutable
import androidx.annotation.DrawableRes

@Immutable
data class Runner(
    val dossardId: Int,
    val firstName: String,
    @DrawableRes val photoResId: Int
)

