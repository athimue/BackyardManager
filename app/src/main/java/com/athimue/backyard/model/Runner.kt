package com.athimue.backyard.model

import androidx.compose.runtime.Immutable
import androidx.annotation.DrawableRes

@Immutable
data class Runner(
    val id: Int,
    val firstName: String,
    @DrawableRes val photoResId: Int
)

