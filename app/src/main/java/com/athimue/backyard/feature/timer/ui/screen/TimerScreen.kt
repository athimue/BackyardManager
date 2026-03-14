package com.athimue.backyard.feature.timer.ui.screen

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
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Text
import com.athimue.backyard.R
import com.athimue.backyard.theme.AppColors
import com.athimue.backyard.theme.AppTypography
import com.athimue.backyard.feature.timer.ui.viewmodel.TimerViewModel

@Composable
fun TimerScreen(
    viewModel: TimerViewModel = hiltViewModel(),
    onShowResults: () -> Unit = {},
    onOpenSettings: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    // Flash background orange briefly when a new lap starts
    val flashColor by animateColorAsState(
        targetValue = if (uiState.lapJustChanged) AppColors.OrangeDim else AppColors.Black,
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
                painter = painterResource(R.drawable.logo),
                contentDescription = null
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "BACKYARD DU GARAGE",
                    fontSize = AppTypography.titleSize,
                    fontFamily = AppTypography.fontFamily,
                    fontWeight = AppTypography.bold,
                    color = AppColors.Orange,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "1ère édition  ·  17 avril 2026",
                    fontSize = AppTypography.labelSize,
                    fontFamily = AppTypography.fontFamily,
                    fontWeight = AppTypography.semiBold,
                    color = AppColors.WhiteDim,
                    textAlign = TextAlign.Center
                )
            }

            Image(
                modifier = Modifier.size(120.dp),
                painter = painterResource(R.drawable.logo),
                contentDescription = null
            )
        }

        // ── Separator ────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(2.dp)
                .background(AppColors.Orange)
        )

        // ── Clock ────────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = uiState.currentTime,
                fontSize = AppTypography.clockSize,
                fontFamily = AppTypography.fontFamily,
                fontWeight = AppTypography.bold,
                color = AppColors.White,
            )
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
                label = "Lap",
                value = uiState.currentLap.toString()
            )
            LabelValueCell(
                modifier = Modifier.weight(1f),
                label = "Runners",
                value = if (uiState.activeRunnersCount > 0) uiState.activeRunnersCount.toString() else "—"
            )
            LabelValueCell(
                modifier = Modifier.weight(1f),
                label = "Next start",
                value = uiState.remainingTimeFormatted
            )
            LabelValueCell(
                modifier = Modifier.weight(1f),
                label = "Elapsed",
                value = uiState.elapsedRaceFormatted
            )
        }

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
                    text = uiState.remainingTimeFormatted,
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
                    containerColor = AppColors.OrangeDim,
                    focusedContainerColor = AppColors.Orange,
                    contentColor = AppColors.White,
                    focusedContentColor = AppColors.White
                )
            ) {
                Text(
                    text = "📋  Results",
                    fontSize = AppTypography.bodySmallSize,
                    fontFamily = AppTypography.fontFamily,
                    fontWeight = AppTypography.semiBold
                )
            }

            Button(
                onClick = onOpenSettings,
                colors = ButtonDefaults.colors(
                    containerColor = AppColors.SurfaceMid,
                    focusedContainerColor = AppColors.Orange,
                    contentColor = AppColors.White,
                    focusedContentColor = AppColors.White
                )
            ) {
                Text(
                    text = "⚙  Settings",
                    fontSize = AppTypography.bodySmallSize,
                    fontFamily = AppTypography.fontFamily,
                    fontWeight = AppTypography.semiBold
                )
            }
        }
    }
}

@Composable
private fun LabelValueCell(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Column(
        modifier = modifier
            .background(AppColors.SurfaceDark, RoundedCornerShape(8.dp))
            .border(width = 2.dp, color = AppColors.Orange, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label.uppercase(),
            color = AppColors.Gray,
            textAlign = TextAlign.Center,
            fontSize = AppTypography.labelSize,
            fontFamily = AppTypography.fontFamily,
            fontWeight = AppTypography.semiBold,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            color = AppColors.White,
            textAlign = TextAlign.Center,
            fontSize = AppTypography.bodyLargeSize,
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
        targetValue = if (urgent) Color(0xFFE53935) else AppColors.Orange,
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

private val Int.sp get() = TextUnit(this.toFloat(), TextUnitType.Sp)
