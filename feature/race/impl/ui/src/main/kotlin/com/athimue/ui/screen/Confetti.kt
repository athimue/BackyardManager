package com.athimue.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import com.athimue.backyard.core.theme.AppColors
import kotlin.math.sin
import kotlin.random.Random

private class ConfettiPiece(
    val x: Float,          // 0..1 horizontal start
    val delay: Float,      // s before it appears
    val speed: Float,      // screen heights per second
    val sway: Float,       // horizontal sway amplitude (0..1 width)
    val spin: Float,       // degrees per second
    val size: Float,       // px
    val color: Color,
)

/**
 * Lightweight celebratory confetti: a fixed set of pieces falling in a loop.
 * Only a time value changes per frame and it is read inside the Canvas draw lambda,
 * so the animation costs a redraw, never a recomposition.
 */
@Composable
internal fun Confetti(modifier: Modifier = Modifier, count: Int = 90) {
    val colors = listOf(AppColors.Yellow, Color(0xFFFFD700), Color.White, AppColors.GreenFilledBorder, AppColors.OrangeFilledBorder)
    val pieces = remember(count) {
        val rnd = Random(42)
        List(count) {
            ConfettiPiece(
                x = rnd.nextFloat(),
                delay = rnd.nextFloat() * 4f,
                speed = 0.12f + rnd.nextFloat() * 0.18f,
                sway = 0.01f + rnd.nextFloat() * 0.03f,
                spin = 90f + rnd.nextFloat() * 270f,
                size = 8f + rnd.nextFloat() * 10f,
                color = colors[rnd.nextInt(colors.size)],
            )
        }
    }
    val time = remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        val start = withFrameNanos { it }
        while (true) {
            withFrameNanos { now -> time.floatValue = (now - start) / 1_000_000_000f }
        }
    }

    Canvas(modifier = modifier) {
        val t = time.floatValue
        pieces.forEach { p ->
            val local = t - p.delay
            if (local < 0f) return@forEach
            // Loop: height + margin travelled at p.speed screen heights / s
            val progress = (local * p.speed) % 1.2f
            val y = (progress - 0.1f) * size.height
            val x = (p.x + sin(local * 2f + p.x * 10f) * p.sway) * size.width
            rotate(degrees = local * p.spin, pivot = Offset(x, y)) {
                drawRect(
                    color = p.color,
                    topLeft = Offset(x - p.size / 2f, y - p.size / 4f),
                    size = Size(p.size, p.size / 2f),
                )
            }
        }
    }
}
