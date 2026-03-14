package com.athimue.backyard.feature.race.impl.domain.model

import androidx.annotation.DrawableRes

data class Runner(
    val dossardId: Int,
    val firstName: String,
    @DrawableRes val photoResId: Int
)