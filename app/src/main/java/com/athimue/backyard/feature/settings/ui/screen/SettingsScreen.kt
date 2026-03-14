package com.athimue.backyard.feature.settings.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Text
import com.athimue.backyard.theme.AppColors
import com.athimue.backyard.theme.AppTypography
import com.athimue.backyard.feature.settings.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onRaceReset: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val resetDone by viewModel.resetDone.collectAsState()

    LaunchedEffect(resetDone) {
        if (resetDone) {
            viewModel.onResetDoneConsumed()
            onRaceReset()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.Black)
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // ── Header ──────────────────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "⚙  Settings",
                        fontSize = AppTypography.titleSize,
                        fontFamily = AppTypography.fontFamily,
                        fontWeight = AppTypography.bold,
                        color = AppColors.Orange
                    )
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.colors(
                            containerColor = AppColors.SurfaceMid,
                            focusedContainerColor = AppColors.Orange
                        )
                    ) {
                        Text("← Back", fontFamily = AppTypography.fontFamily)
                    }
                }

                Spacer(Modifier.height(4.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(AppColors.Orange)
                )
            }

            // ── Start time ──────────────────────────────────────────────────────
            item {
                SectionTitle("Race Start Time")
                Spacer(Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Hours
                    Text("Hours:", color = AppColors.Gray, fontFamily = AppTypography.fontFamily)
                    StepButton("-") { viewModel.decrementStartHour() }
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(AppColors.SurfaceDark, RoundedCornerShape(8.dp))
                            .border(1.dp, AppColors.Orange, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "%02d".format(uiState.startHour),
                            color = AppColors.White,
                            fontSize = AppTypography.bodyLargeSize,
                            fontFamily = AppTypography.fontFamily,
                            fontWeight = AppTypography.bold
                        )
                    }
                    StepButton("+") { viewModel.incrementStartHour() }

                    Spacer(Modifier.width(16.dp))

                    // Minutes
                    Text("Minutes:", color = AppColors.Gray, fontFamily = AppTypography.fontFamily)
                    StepButton("-") { viewModel.decrementStartMinute() }
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(AppColors.SurfaceDark, RoundedCornerShape(8.dp))
                            .border(1.dp, AppColors.Orange, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "%02d".format(uiState.startMinute),
                            color = AppColors.White,
                            fontSize = AppTypography.bodyLargeSize,
                            fontFamily = AppTypography.fontFamily,
                            fontWeight = AppTypography.bold
                        )
                    }
                    StepButton("+") { viewModel.incrementStartMinute() }

                    Spacer(Modifier.width(24.dp))
                    Text(
                        text = "→  ${uiState.startTimeFormatted}",
                        color = AppColors.Orange,
                        fontSize = AppTypography.bodyMediumSize,
                        fontFamily = AppTypography.fontFamily,
                        fontWeight = AppTypography.bold
                    )
                }
            }

            // ── Runners ─────────────────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionTitle("Runners (${uiState.runners.size})")
                    Button(
                        onClick = { viewModel.restoreDefaultRunners() },
                        colors = ButtonDefaults.colors(
                            containerColor = AppColors.SurfaceMid,
                            focusedContainerColor = AppColors.OrangeDim
                        )
                    ) {
                        Text(
                            "↺  Restore defaults",
                            fontFamily = AppTypography.fontFamily,
                            fontSize = AppTypography.labelSize
                        )
                    }
                }
            }

            items(uiState.runners) { runner ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppColors.SurfaceCell, RoundedCornerShape(6.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = runner.firstName,
                        color = AppColors.White,
                        fontSize = AppTypography.bodySmallSize,
                        fontFamily = AppTypography.fontFamily
                    )
                    Button(
                        onClick = { viewModel.removeRunner(runner.dossardId) },
                        colors = ButtonDefaults.colors(
                            containerColor = AppColors.RedFilled,
                            focusedContainerColor = AppColors.RedFilledBorder
                        )
                    ) {
                        Text(
                            "✕ Remove",
                            fontFamily = AppTypography.fontFamily,
                            fontSize = AppTypography.labelSize
                        )
                    }
                }
            }

            // ── Danger zone ──────────────────────────────────────────────────────
            item {
                SectionTitle("Danger Zone")
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = { viewModel.resetRace() },
                    colors = ButtonDefaults.colors(
                        containerColor = AppColors.RedFilled,
                        focusedContainerColor = AppColors.RedFilledBorder,
                        contentColor = AppColors.White,
                        focusedContentColor = AppColors.White
                    )
                ) {
                    Text(
                        text = "🔄  Reset race (clears all results)",
                        fontSize = AppTypography.bodyMediumSize,
                        fontFamily = AppTypography.fontFamily,
                        fontWeight = AppTypography.bold
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text.uppercase(),
        color = AppColors.Orange,
        fontSize = AppTypography.labelSize,
        fontFamily = AppTypography.fontFamily,
        fontWeight = AppTypography.semiBold,
        letterSpacing = 2.sp
    )
}

@Composable
private fun StepButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.colors(
            containerColor = AppColors.SurfaceMid,
            focusedContainerColor = AppColors.Orange
        )
    ) {
        Text(
            text = label,
            fontSize = AppTypography.bodyLargeSize,
            fontFamily = AppTypography.fontFamily,
            fontWeight = AppTypography.bold,
            textAlign = TextAlign.Center
        )
    }
}

private val Int.sp get() = TextUnit(this.toFloat(), TextUnitType.Sp)
