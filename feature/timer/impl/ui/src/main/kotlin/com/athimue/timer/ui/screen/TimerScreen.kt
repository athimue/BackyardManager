package com.athimue.timer.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Text
import com.athimue.backyard.core.EVENT_NAME
import com.athimue.backyard.core.EVENT_SUBTITLE
import com.athimue.backyard.core.theme.AppColors
import com.athimue.backyard.core.theme.AppTypography
import com.athimue.timer.ui.viewmodel.TimerViewModel
import com.athimue.backyard.core.theme.R as CoreR

@Composable
fun TimerScreen(
    viewModel: TimerViewModel = hiltViewModel(),
    onShowResults: () -> Unit = {},
    onOpenSettings: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    // Flash background orange briefly when a new lap starts
    val flashColor by animateColorAsState(
        targetValue = if (uiState.lapJustChanged) AppColors.Yellow else AppColors.Black,
        animationSpec = tween(400),
        label = "lap_flash"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(flashColor)
            .padding(horizontal = 32.dp, vertical = 8.dp)
    ) {
        // ── Header ──────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier.size(120.dp),
                painter = painterResource(CoreR.drawable.logo),
                contentDescription = null
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = EVENT_NAME,
                    fontSize = AppTypography.titleSize,
                    fontFamily = AppTypography.titleFontFamily,
                    fontWeight = AppTypography.bold,
                    color = AppColors.Yellow,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = EVENT_SUBTITLE,
                    fontSize = AppTypography.labelSize,
                    fontFamily = AppTypography.fontFamily,
                    fontWeight = AppTypography.semiBold,
                    color = AppColors.WhiteDim,
                    textAlign = TextAlign.Center
                )
            }

            Image(
                modifier = Modifier.size(120.dp),
                painter = painterResource(CoreR.drawable.logo),
                contentDescription = null
            )
        }

        // ── Separator ────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(2.dp)
                .background(AppColors.Yellow)
        )

        // ── Prochain départ (main) + Clock ───────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 16.dp),
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "PROCHAIN DÉPART",
                    color = AppColors.Gray,
                    textAlign = TextAlign.Center,
                    fontSize = AppTypography.bodySmallSize,
                    fontFamily = AppTypography.fontFamily,
                    fontWeight = AppTypography.semiBold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.remainingTimeFormatted,
                    color = AppColors.White,
                    textAlign = TextAlign.Center,
                    fontSize = 100.sp,
                    fontFamily = AppTypography.fontFamily,
                    fontWeight = AppTypography.bold,
                )
            }
            Column(
                modifier = Modifier.align(Alignment.TopEnd),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = uiState.currentTime,
                    color = AppColors.WhiteDim,
                    textAlign = TextAlign.Center,
                    fontSize = AppTypography.bodyMediumSize,
                    fontFamily = AppTypography.fontFamily,
                    fontWeight = AppTypography.normal,
                )
            }
        }

        // ── Info cells ───────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LabelValueCell(
                modifier = Modifier.weight(1f),
                label = "Tour",
                value = uiState.currentLap.toString(),
                labelTextSize = 12.sp,
                valueTextSize = 28.sp
            )
            LabelValueCell(
                modifier = Modifier.weight(1f),
                label = "Coureurs",
                value = if (uiState.activeRunnersCount > 0) uiState.activeRunnersCount.toString() else "—",
                labelTextSize = 12.sp,
                valueTextSize = 28.sp
            )
            LabelValueCell(
                modifier = Modifier.weight(1f),
                label = "Temps écoulé",
                value = uiState.elapsedRaceFormatted,
                labelTextSize = 12.sp,
                valueTextSize = 28.sp
            )
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            // ── Progress bar ─────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                ProgressBar(
                    progress = uiState.lapProgress,
                    urgent = uiState.isLapEndUrgent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                )

                if (uiState.showEndLapCountdown) {
                    Text(
                        text = uiState.remainingSecondsFormatted,
                        color = AppColors.White,
                        fontSize = AppTypography.chronoSize,
                        fontWeight = AppTypography.bold,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        fontFamily = AppTypography.fontFamily,
                    )
                }
            }

            // ── Action buttons ───────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                Button(
                    onClick = onShowResults,
                    colors = ButtonDefaults.colors(
                        containerColor = AppColors.Yellow,
                        focusedContainerColor = AppColors.Yellow,
                        contentColor = AppColors.White,
                        focusedContentColor = AppColors.White
                    )
                ) {
                    Text(
                        text = "📋  Résultats",
                        fontSize = AppTypography.bodySmallSize,
                        fontFamily = AppTypography.fontFamily,
                        fontWeight = AppTypography.semiBold
                    )
                }

                Button(
                    onClick = onOpenSettings,
                    colors = ButtonDefaults.colors(
                        containerColor = AppColors.SurfaceMid,
                        focusedContainerColor = AppColors.Yellow,
                        contentColor = AppColors.White,
                        focusedContentColor = AppColors.White
                    )
                ) {
                    Text(
                        text = "⚙  Réglages",
                        fontSize = AppTypography.bodySmallSize,
                        fontFamily = AppTypography.fontFamily,
                        fontWeight = AppTypography.semiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun LabelValueCell(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    labelTextSize: TextUnit = AppTypography.labelSize,
    valueTextSize: TextUnit = AppTypography.bodyLargeSize
) {
    Column(
        modifier = modifier
            .background(AppColors.SurfaceDark, RoundedCornerShape(8.dp))
            .border(width = 2.dp, color = AppColors.Yellow, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label.uppercase(),
            color = AppColors.Gray,
            textAlign = TextAlign.Center,
            fontSize = labelTextSize,
            fontFamily = AppTypography.fontFamily,
            fontWeight = AppTypography.semiBold,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            color = AppColors.White,
            textAlign = TextAlign.Center,
            fontSize = valueTextSize,
            fontFamily = AppTypography.fontFamily,
            fontWeight = AppTypography.bold,
        )
    }
}

@Composable
private fun ProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
    urgent: Boolean = false,
) {
    val fillColor by animateColorAsState(
        targetValue = if (urgent) Color(0xFFE53935) else AppColors.Yellow,
        animationSpec = tween(600),
        label = "progress_color"
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(AppColors.GraySubtle)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .fillMaxHeight()
                .clip(RoundedCornerShape(4.dp))
                .background(fillColor)
        )
    }
}


