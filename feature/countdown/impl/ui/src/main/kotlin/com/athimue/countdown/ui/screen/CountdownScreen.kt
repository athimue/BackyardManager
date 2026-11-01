package com.athimue.backyard.feature.countdown.impl.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Text
import com.athimue.backyard.core.EVENT_NAME
import com.athimue.backyard.core.EVENT_SUBTITLE
import com.athimue.backyard.core.SECONDS_PER_HOUR
import com.athimue.backyard.core.SECONDS_PER_MINUTE
import com.athimue.backyard.core.theme.AppColors
import com.athimue.backyard.core.theme.AppEffects
import com.athimue.backyard.core.theme.AppTypography
import com.athimue.backyard.core.theme.backyardBackground
import com.athimue.backyard.core.theme.components.BackyardButton
import com.athimue.backyard.core.theme.components.BackyardButtonStyle
import com.athimue.backyard.feature.countdown.impl.ui.model.RaceStateUiModel.FINISHED
import com.athimue.backyard.feature.countdown.impl.ui.model.RaceStateUiModel.IN_PROGRESS
import com.athimue.backyard.feature.countdown.impl.ui.viewmodel.CountdownViewModel
import com.athimue.backyard.core.theme.R as CoreR

@Composable
internal fun CountdownScreen(
    viewModel: CountdownViewModel = hiltViewModel(),
    onRaceStarted: () -> Unit,
    onRaceFinished: () -> Unit = {},
    onOpenSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Auto-navigate when race starts (or straight back to the podium if the app
    // is relaunched after the race has finished)
    LaunchedEffect(uiState.raceState) {
        when (uiState.raceState) {
            IN_PROGRESS -> onRaceStarted()
            FINISHED -> onRaceFinished()
            else -> Unit
        }
    }

    val total = uiState.countdownSeconds
    val hours = total / SECONDS_PER_HOUR
    val minutes = (total % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE
    val seconds = total % SECONDS_PER_MINUTE
    // Colons blink with the seconds, like a real start clock (no continuous animation)
    val colonVisible = total % 2L == 0L

    Column(
        modifier = Modifier
            .fillMaxSize()
            .backyardBackground()
            .padding(horizontal = 48.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // ── Header: same language as the race clock screen ─────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(CoreR.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Spacer(Modifier.width(24.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = EVENT_NAME,
                    fontSize = AppTypography.titleSize,
                    fontFamily = AppTypography.titleFontFamily,
                    fontWeight = AppTypography.bold,
                    color = AppColors.Yellow,
                    style = AppEffects.glow(AppColors.Yellow, blurRadius = 24f),
                )
                Text(
                    text = EVENT_SUBTITLE,
                    fontSize = AppTypography.bodySmallSize,
                    fontFamily = AppTypography.fontFamily,
                    color = AppColors.WhiteDim,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = uiState.currentTime,
                    fontSize = AppTypography.bodyLargeSize,
                    fontFamily = AppTypography.fontFamily,
                    fontWeight = AppTypography.bold,
                    color = AppColors.White,
                )
                Text(
                    text = "Heure actuelle",
                    fontSize = AppTypography.labelSize,
                    fontFamily = AppTypography.fontFamily,
                    color = AppColors.Gray,
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(2.dp)
                .background(AppColors.Yellow)
        )

        // ── Start board ────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Départ dans",
                fontSize = AppTypography.subtitleSize,
                fontFamily = AppTypography.fontFamily,
                color = AppColors.WhiteDim,
            )
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.Top) {
                DigitTile(value = hours, label = "heures")
                Colon(visible = colonVisible)
                DigitTile(value = minutes, label = "minutes")
                Colon(visible = colonVisible)
                DigitTile(value = seconds, label = "secondes")
            }
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Départ prévu à ${uiState.startTimeFormatted}",
                fontSize = AppTypography.bodySmallSize,
                fontFamily = AppTypography.fontFamily,
                color = AppColors.Yellow,
                modifier = Modifier
                    .background(AppColors.Yellow.copy(alpha = 0.10f), RoundedCornerShape(50))
                    .border(1.dp, AppColors.Yellow.copy(alpha = 0.5f), RoundedCornerShape(50))
                    .padding(horizontal = 24.dp, vertical = 10.dp),
            )
        }

        // ── Actions ────────────────────────────────────────────────────────────
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            BackyardButton(
                text = "Démarrer maintenant",
                icon = Icons.Default.PlayArrow,
                style = BackyardButtonStyle.Start,
                fontSize = AppTypography.bodyMediumSize,
                onClick = { viewModel.startRaceNow() },
            )
            BackyardButton(
                text = "Réglages",
                icon = Icons.Default.Settings,
                style = BackyardButtonStyle.Secondary,
                fontSize = AppTypography.bodyMediumSize,
                onClick = onOpenSettings,
            )
        }
    }
}

/** One block of the start board: two big glowing digits on a dark plate. */
@Composable
private fun DigitTile(value: Long, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .widthIn(min = 200.dp)
                .background(AppColors.SurfaceDark, RoundedCornerShape(20.dp))
                .border(1.dp, AppColors.GraySubtle, RoundedCornerShape(20.dp))
                .padding(horizontal = 24.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "%02d".format(value),
                fontSize = 110.sp,
                fontFamily = AppTypography.fontFamily,
                fontWeight = AppTypography.bold,
                color = AppColors.Yellow,
                style = AppEffects.glow(AppColors.Yellow, blurRadius = 48f),
                textAlign = TextAlign.Center,
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = AppTypography.bodySmallSize,
            fontFamily = AppTypography.fontFamily,
            color = AppColors.Gray,
        )
    }
}

@Composable
private fun Colon(visible: Boolean) {
    Text(
        text = ":",
        fontSize = 96.sp,
        fontFamily = AppTypography.fontFamily,
        fontWeight = AppTypography.bold,
        color = AppColors.Yellow,
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .graphicsLayer { alpha = if (visible) 1f else 0.25f },
    )
}
