package com.athimue.backyard.feature.countdown.impl.ui.screen

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Text
import com.athimue.backyard.R
import com.athimue.backyard.feature.countdown.impl.ui.model.RaceStateUiModel.IN_PROGRESS
import com.athimue.backyard.feature.countdown.impl.ui.viewmodel.CountdownViewModel
import com.athimue.backyard.core.theme.AppColors
import com.athimue.backyard.core.theme.AppTypography

@Composable
internal fun CountdownScreen(
    viewModel: CountdownViewModel = hiltViewModel(),
    onRaceStarted: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Auto-navigate when race starts
    LaunchedEffect(uiState.raceState) {
        if (uiState.raceState == IN_PROGRESS) onRaceStarted()
    }

    val pulse by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 1f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "pulse_alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Black)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // ── Header ──────────────────────────────────────────────────────────
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Spacer(Modifier.height(8.dp))
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
                color = AppColors.WhiteDim,
                textAlign = TextAlign.Center
            )
        }

        // ── Countdown ────────────────────────────────────────────────────────
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Race starts at ${uiState.startTimeFormatted}",
                fontSize = AppTypography.bodyMediumSize,
                fontFamily = AppTypography.fontFamily,
                color = AppColors.Gray,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = uiState.countdownFormatted,
                fontSize = 96.sp,
                fontFamily = AppTypography.fontFamily,
                fontWeight = AppTypography.bold,
                color = AppColors.Orange,
                modifier = Modifier.alpha(pulse),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Current time: ${uiState.currentTime}",
                fontSize = AppTypography.bodySmallSize,
                fontFamily = AppTypography.fontFamily,
                color = AppColors.WhiteDim,
                textAlign = TextAlign.Center
            )
        }

        // ── Actions ──────────────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally)
        ) {
            Button(
                onClick = { viewModel.startRaceNow() },
                colors = ButtonDefaults.colors(
                    containerColor = AppColors.GreenFilled,
                    focusedContainerColor = AppColors.GreenFilledBorder,
                    contentColor = AppColors.White,
                    focusedContentColor = AppColors.White
                )
            ) {
                Text(
                    text = "▶  Start now",
                    fontSize = AppTypography.bodyMediumSize,
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
                    fontSize = AppTypography.bodyMediumSize,
                    fontFamily = AppTypography.fontFamily,
                    fontWeight = AppTypography.semiBold
                )
            }
        }
    }
}
