package com.athimue.backyard.core.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle

/**
 * App-wide background: black with a soft yellow halo coming from the top of the screen.
 * Brush is cached and only rebuilt when the size changes.
 */
fun Modifier.backyardBackground(): Modifier = drawWithCache {
    val halo = Brush.radialGradient(
        colors = listOf(AppColors.Yellow.copy(alpha = 0.11f), Color.Transparent),
        center = Offset(size.width / 2f, 0f),
        radius = size.maxDimension * 0.65f,
    )
    onDrawBehind {
        drawRect(AppColors.Black)
        drawRect(halo)
    }
}

object AppEffects {
    /** Neon-like glow for big numbers / titles. */
    fun glow(color: Color, blurRadius: Float = 32f): TextStyle = TextStyle(
        shadow = Shadow(color = color.copy(alpha = 0.55f), offset = Offset.Zero, blurRadius = blurRadius)
    )
}
