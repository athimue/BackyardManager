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
import androidx.compose.foundation.layout.width
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
import com.athimue.backyard.core.theme.AppColors
import com.athimue.backyard.core.theme.AppTypography
import com.athimue.backyard.core.theme.R as CoreR
import com.athimue.backyard.feature.countdown.impl.ui.model.RaceStateUiModel.IN_PROGRESS
import com.athimue.backyard.feature.countdown.impl.ui.viewmodel.CountdownViewModel

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
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // ── Header ──────────────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(CoreR.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Spacer(Modifier.width(48.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "BACKYARD DU GARAGE",
                    fontSize = AppTypography.titleSize,
                    fontFamily = AppTypography.titleFontFamily,
                    fontWeight = AppTypography.bold,
                    color = AppColors.Yellow,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = "1ère édition - 17 avril 2026",
                    fontSize = 18.sp,
                    fontFamily = AppTypography.fontFamily,
                    color = AppColors.WhiteDim,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.width(48.dp))
            Image(
                painter = painterResource(CoreR.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }

        // ── Countdown ────────────────────────────────────────────────────────
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Début de la course à ${uiState.startTimeFormatted}",
                fontSize = AppTypography.bodyMediumSize,
                fontFamily = AppTypography.fontFamily,
                color = AppColors.Gray,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = uiState.countdownFormatted,
                fontSize = 130.sp,
                fontFamily = AppTypography.fontFamily,
                fontWeight = AppTypography.bold,
                color = AppColors.Yellow,
                modifier = Modifier.alpha(pulse),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Heure actuel : ${uiState.currentTime}",
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
                    focusedContainerColor = AppColors.Yellow,
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
