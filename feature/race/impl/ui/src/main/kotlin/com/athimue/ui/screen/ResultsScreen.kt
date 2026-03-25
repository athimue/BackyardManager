package com.athimue.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.DirectionCenter
import androidx.compose.ui.input.key.Key.Companion.Enter
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.athimue.backyard.core.EVENT_NAME
import com.athimue.backyard.core.EVENT_SUBTITLE
import com.athimue.backyard.core.theme.AppColors
import com.athimue.backyard.core.theme.AppTypography
import com.athimue.ui.model.LapStatusUiModel
import com.athimue.ui.model.RunnerUiModel
import com.athimue.ui.viewmodel.ResultsViewModel
import com.athimue.backyard.core.theme.R as CoreR

private val RUNNER_CELL_WIDTH = 140.dp
private val LAP_CELL_HEIGHT = 24.dp
private val LIFELINE_HEIGHT = 30.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ResultsScreen(
    viewModel: ResultsViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onOpenSettings: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    val firstCellRequester = remember { FocusRequester() }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Black)
            .padding(all = 2.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        item { ResultsHeader(onBack = onBack, onOpenSettings = onOpenSettings) }

        // ── Stats bar ────────────────────────────────────────────────────────
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.SurfaceDark, RoundedCornerShape(6.dp))
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                StatChip(
                    label = "Active",
                    value = uiState.activeRunnersCount.toString(),
                    valueColor = AppColors.GreenFilledBorder
                )
                StatChip(
                    label = "Eliminated",
                    value = uiState.eliminatedRunnersCount.toString(),
                    valueColor = AppColors.RedFilledBorder
                )
                StatChip(
                    label = "Total",
                    value = uiState.runners.size.toString(),
                    valueColor = AppColors.White
                )
            }
        }

        // ── Lap number header row ────────────────────────────────────────────
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.width(RUNNER_CELL_WIDTH))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    uiState.laps.forEach { lap ->
                        val isCurrentLap = lap == uiState.currentLap
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .then(
                                    if (isCurrentLap) Modifier.background(
                                        color = AppColors.Yellow,
                                        shape = RoundedCornerShape(4.dp)
                                    ) else Modifier
                                ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = lap.toString(),
                                color = AppColors.Gray,
                                fontSize = AppTypography.bodySmallSize,
                                fontFamily = AppTypography.fontFamily,
                                fontWeight = if (isCurrentLap) AppTypography.bold else AppTypography.semiBold
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(AppColors.GraySubtle)
            )
        }

        // ── Runner rows ──────────────────────────────────────────────────────
        itemsIndexed(uiState.runners) { runnerIndex, runner ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.SurfaceCell, RoundedCornerShape(4.dp))
                    .padding(vertical = 2.dp)
                    .focusGroup(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Runner identity
                Column(
                    modifier = Modifier
                        .width(RUNNER_CELL_WIDTH)
                        .padding(end = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { viewModel.onRunnerNameClicked(runner.dossardId) },
                        colors = ButtonDefaults.colors(
                            containerColor = AppColors.SurfaceMid,
                            focusedContainerColor = AppColors.Yellow,
                            contentColor = AppColors.Yellow,
                            focusedContentColor = AppColors.White
                        )
                    ) {
                        Text(
                            text = runner.firstName,
                            fontSize = AppTypography.labelSize,
                            fontFamily = AppTypography.fontFamily,
                            fontWeight = AppTypography.bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${uiState.completedLapsFor(runner.dossardId)} laps",
                            color = AppColors.Gray,
                            fontSize = AppTypography.labelSize,
                            fontFamily = AppTypography.fontFamily
                        )
                        uiState.bestLapTimeFor(runner.dossardId)?.let { best ->
                            Text(
                                text = "· $best",
                                color = AppColors.GreenFilledBorder,
                                fontSize = AppTypography.labelSize,
                                fontFamily = AppTypography.fontFamily,
                                fontWeight = AppTypography.semiBold
                            )
                        }
                    }
                }

                // Lap cells + lifeline
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        uiState.laps.forEachIndexed { lapIndex, lap ->
                            val lapResult = uiState.lapResultFor(runner.dossardId, lap)
                            val isCross = uiState.isCrossCell(runner.dossardId, lap)
                            var isFocused by remember { mutableStateOf(false) }

                            val cellBackground = when {
                                isCross -> AppColors.RedFilled
                                isFocused -> AppColors.Yellow
                                lapResult?.status == LapStatusUiModel.COMPLETED -> AppColors.GreenFilled
                                lapResult?.status == LapStatusUiModel.ELIMINATED -> AppColors.RedFilled
                                else -> AppColors.SurfaceMid
                            }
                            val cellBorder = when {
                                isFocused -> AppColors.Yellow
                                isCross -> AppColors.RedFilledBorder
                                lapResult?.status == LapStatusUiModel.COMPLETED -> AppColors.GreenFilledBorder
                                lapResult?.status == LapStatusUiModel.ELIMINATED -> AppColors.RedFilledBorder
                                else -> AppColors.GraySubtle
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(LAP_CELL_HEIGHT)
                                    .then(
                                        if (runnerIndex == 0 && lapIndex == 0)
                                            Modifier
                                                .focusRequester(firstCellRequester)
                                                .bringIntoViewRequester(bringIntoViewRequester)
                                        else Modifier
                                    )
                                    .onFocusChanged { isFocused = it.isFocused }
                                    .onPreviewKeyEvent {
                                        if (
                                            it.type == KeyEventType.KeyUp &&
                                            (it.key == Enter || it.key == DirectionCenter)
                                        ) {
                                            viewModel.onLapClicked(
                                                runnerId = runner.dossardId,
                                                lapNumber = lap
                                            )
                                            true
                                        } else false
                                    }
                                    .focusable()
                                    .background(cellBackground, RoundedCornerShape(4.dp))
                                    .border(
                                        width = if (isFocused) 2.dp else 1.dp,
                                        color = cellBorder,
                                        shape = RoundedCornerShape(4.dp)
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                when {
                                    isCross -> Text(
                                        text = "✕",
                                        color = AppColors.RedFilledBorder,
                                        fontSize = AppTypography.bodySmallSize,
                                        fontWeight = FontWeight.Bold
                                    )

                                    lapResult?.status == LapStatusUiModel.ELIMINATED -> Text(
                                        text = lapResult.time.ifBlank { "DNF" },
                                        color = AppColors.White,
                                        fontSize = AppTypography.labelSize,
                                        fontFamily = AppTypography.fontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1
                                    )

                                    lapResult?.status == LapStatusUiModel.COMPLETED -> Text(
                                        text = lapResult.time,
                                        color = AppColors.White,
                                        fontSize = AppTypography.labelSize,
                                        fontFamily = AppTypography.fontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }

                    RunnerLifeLine(
                        modifier = Modifier.padding(top = 4.dp),
                        runner = runner,
                        completedLaps = uiState.completedLapsFor(runner.dossardId),
                        totalLaps = uiState.laps.size
                    )
                }
            }
        }

        // Bottom padding
        item { Box(Modifier.height(16.dp)) }
    }

    LaunchedEffect(Unit) {
        bringIntoViewRequester.bringIntoView()
        firstCellRequester.requestFocus()
    }
}

@Composable
private fun StatChip(label: String, value: String, valueColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "$label:",
            color = AppColors.Gray,
            fontSize = AppTypography.labelSize,
            fontFamily = AppTypography.fontFamily
        )
        Text(
            text = value,
            color = valueColor,
            fontSize = AppTypography.bodySmallSize,
            fontFamily = AppTypography.fontFamily,
            fontWeight = AppTypography.bold
        )
    }
}

@Composable
private fun ResultsHeader(
    onBack: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(CoreR.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(44.dp)
        )

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = EVENT_NAME,
                fontSize = AppTypography.bodyLargeSize,
                fontFamily = AppTypography.titleFontFamily,
                fontWeight = AppTypography.bold,
                color = AppColors.Yellow,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = EVENT_SUBTITLE,
                fontSize = AppTypography.labelSize,
                fontFamily = AppTypography.fontFamily,
                color = AppColors.Yellow
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Column(
            modifier = Modifier.width(160.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.colors(
                        containerColor = AppColors.SurfaceMid,
                        focusedContainerColor = AppColors.Yellow
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = AppColors.GraySubtle
                    )
                }
                Button(
                    onClick = onOpenSettings,
                    colors = ButtonDefaults.colors(
                        containerColor = AppColors.SurfaceMid,
                        focusedContainerColor = AppColors.Yellow
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = AppColors.GraySubtle
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(AppColors.Yellow)
    )
}

@Composable
private fun RunnerLifeLine(
    runner: RunnerUiModel,
    completedLaps: Int,
    totalLaps: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        BoxWithConstraints(
            modifier = modifier
                .fillMaxWidth()
                .height(LIFELINE_HEIGHT)
                .padding(horizontal = 2.dp, vertical = 2.dp)
        ) {
            val cellWidth = if (totalLaps > 0) maxWidth / totalLaps else maxWidth
            val photoSize = 200.dp
            val photoRadius = photoSize / 2

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .align(Alignment.CenterStart)
                    .background(AppColors.GraySubtle, RoundedCornerShape(2.dp))
            )

            if (completedLaps > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(completedLaps.toFloat() / totalLaps)
                        .height(2.dp)
                        .align(Alignment.CenterStart)
                        .background(AppColors.Yellow, RoundedCornerShape(2.dp))
                )
            }

            val photoOffsetX = if (completedLaps > 0) {
                val raw = cellWidth * (completedLaps - 0.5f) - photoRadius
                raw.coerceIn(0.dp, maxWidth - photoSize)
            } else {
                0.dp
            }

            Image(
                painter = painterResource(runner.photoResId),
                contentDescription = runner.firstName,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(photoSize)
                    .offset(x = photoOffsetX)
                    .align(Alignment.CenterStart)
            )
        }
    }
}
