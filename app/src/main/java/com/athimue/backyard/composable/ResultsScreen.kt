package com.athimue.backyard.composable

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
import androidx.tv.material3.Text
import com.athimue.backyard.R
import com.athimue.backyard.model.LapStatus
import com.athimue.backyard.ui.theme.AppColors
import com.athimue.backyard.ui.theme.AppTypography
import com.athimue.backyard.viewmodel.ResultsViewModel

private val RUNNER_CELL_WIDTH = 140.dp
private val LAP_CELL_HEIGHT = 48.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResultsScreen(
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
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        item { ResultsHeader(onBack = onBack, onOpenSettings = onOpenSettings) }

        // ── Stats bar ────────────────────────────────────────────────────────
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.SurfaceDark, RoundedCornerShape(6.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
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
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = lap.toString(),
                                color = AppColors.Orange,
                                fontSize = AppTypography.bodySmallSize,
                                fontFamily = AppTypography.fontFamily,
                                fontWeight = AppTypography.semiBold
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
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            AppColors.SurfaceCell,
                            RoundedCornerShape(4.dp)
                        )
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
                            onClick = { viewModel.onRunnerNameClicked(runner.id) },
                            colors = ButtonDefaults.colors(
                                containerColor = AppColors.SurfaceMid,
                                focusedContainerColor = AppColors.OrangeDim,
                                contentColor = AppColors.Orange,
                                focusedContentColor = AppColors.White
                            )
                        ) {
                            Text(
                                text = runner.firstName,
                                fontSize = AppTypography.bodySmallSize,
                                fontFamily = AppTypography.fontFamily,
                                fontWeight = AppTypography.bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            modifier = Modifier.padding(top = 6.dp),
                            text = "${uiState.completedLapsFor(runner.id)} laps",
                            color = AppColors.Gray,
                            fontSize = AppTypography.labelSize,
                            fontFamily = AppTypography.fontFamily
                        )
                    }

                    // Lap cells
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        uiState.laps.forEachIndexed { lapIndex, lap ->
                            val lapResult = uiState.lapResultFor(runner.id, lap)
                            val isCross = uiState.isCrossCell(runner.id, lap)
                            var isFocused by remember { mutableStateOf(false) }

                            val cellBackground = when {
                                isCross -> AppColors.RedFilled
                                isFocused -> AppColors.OrangeSubtle
                                lapResult?.status == LapStatus.COMPLETED -> AppColors.GreenFilled
                                lapResult?.status == LapStatus.ELIMINATED -> AppColors.RedFilled
                                else -> AppColors.SurfaceMid
                            }
                            val cellBorder = when {
                                isFocused -> AppColors.Orange
                                isCross -> AppColors.RedFilledBorder
                                lapResult?.status == LapStatus.COMPLETED -> AppColors.GreenFilledBorder
                                lapResult?.status == LapStatus.ELIMINATED -> AppColors.RedFilledBorder
                                else -> AppColors.GraySubtle
                            }

                            Box(
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
                                                runnerId = runner.id,
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
                                contentAlignment = Alignment.Center
                            ) {
                                when {
                                    isCross -> Text(
                                        text = "✕",
                                        color = AppColors.RedFilledBorder,
                                        fontSize = AppTypography.bodySmallSize,
                                        fontWeight = FontWeight.Bold
                                    )

                                    lapResult?.status == LapStatus.ELIMINATED -> Text(
                                        text = lapResult.time.ifBlank { "DNF" },
                                        color = AppColors.White,
                                        fontSize = AppTypography.labelSize,
                                        fontFamily = AppTypography.fontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1
                                    )

                                    lapResult?.status == LapStatus.COMPLETED -> Text(
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
                }

                RunnerLifeLine(
                    runner = runner,
                    completedLaps = uiState.completedLapsFor(runner.id),
                    totalLaps = uiState.laps.size
                )
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
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(72.dp)
        )

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "BACKYARD DU GARAGE",
                fontSize = AppTypography.titleSize,
                fontFamily = AppTypography.fontFamily,
                fontWeight = AppTypography.bold,
                color = AppColors.Orange,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .width(160.dp)
                .padding(start = 8.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "17 avril 2026",
                fontSize = AppTypography.bodySmallSize,
                fontFamily = AppTypography.fontFamily,
                fontWeight = AppTypography.semiBold,
                color = AppColors.White,
                textAlign = TextAlign.End
            )
            Text(
                text = "1ère édition",
                fontSize = AppTypography.labelSize,
                fontFamily = AppTypography.fontFamily,
                color = AppColors.Orange,
                textAlign = TextAlign.End
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.colors(
                        containerColor = AppColors.SurfaceMid,
                        focusedContainerColor = AppColors.Orange
                    )
                ) {
                    Text("←", fontFamily = AppTypography.fontFamily)
                }
                Button(
                    onClick = onOpenSettings,
                    colors = ButtonDefaults.colors(
                        containerColor = AppColors.SurfaceMid,
                        focusedContainerColor = AppColors.Orange
                    )
                ) {
                    Text("⚙", fontFamily = AppTypography.fontFamily)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(AppColors.Orange)
    )
}

@Composable
private fun RunnerLifeLine(
    runner: com.athimue.backyard.model.Runner,
    completedLaps: Int,
    totalLaps: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
    ) {
        Spacer(modifier = Modifier.width(RUNNER_CELL_WIDTH))

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 2.dp)
        ) {
            val totalWidth = maxWidth
            val cellWidth = totalWidth / totalLaps
            val photoSize = 36.dp
            val photoRadius = photoSize / 2

            // Background track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .align(Alignment.CenterStart)
                    .background(AppColors.GraySubtle, RoundedCornerShape(2.dp))
            )

            // Completed portion of the track
            if (completedLaps > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(completedLaps.toFloat() / totalLaps)
                        .height(3.dp)
                        .align(Alignment.CenterStart)
                        .background(AppColors.Orange, RoundedCornerShape(2.dp))
                )
            }

            // Runner photo positioned at their last completed lap
            val photoOffsetX = if (completedLaps > 0) {
                maxOf(0.dp, cellWidth * (completedLaps - 0.5f) - photoRadius)
            } else {
                0.dp
            }

            Image(
                painter = painterResource(runner.photoResId),
                contentDescription = runner.firstName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(photoSize)
                    .offset(x = photoOffsetX)
                    .align(Alignment.CenterStart)
                    .clip(CircleShape)
                    .background(AppColors.SurfaceMid)
                    .border(
                        width = 2.dp,
                        color = if (completedLaps > 0) AppColors.Orange else AppColors.GraySubtle,
                        shape = CircleShape
                    )
            )
        }
    }
}
