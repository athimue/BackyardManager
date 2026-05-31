package com.athimue.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.athimue.backyard.core.EVENT_NAME
import com.athimue.backyard.core.EVENT_SUBTITLE
import com.athimue.backyard.core.theme.AppColors
import com.athimue.backyard.core.theme.AppTypography
import com.athimue.ui.model.PodiumEntryUiModel
import com.athimue.ui.viewmodel.FinishViewModel
import kotlinx.coroutines.delay
import com.athimue.backyard.core.theme.R as CoreR

private val Gold = Color(0xFFFFD700)
private val Silver = Color(0xFFC0C0C0)
private val Bronze = Color(0xFFCD7F32)

@Composable
internal fun FinishScreen(
    viewModel: FinishViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Black)
            .padding(horizontal = 32.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item { FinishHeader(onBack = onBack) }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "FIN DE COURSE",
                    fontSize = AppTypography.titleSize,
                    fontFamily = AppTypography.titleFontFamily,
                    fontWeight = AppTypography.bold,
                    color = AppColors.Yellow,
                    letterSpacing = 4.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "PODIUM",
                    fontSize = AppTypography.bodyMediumSize,
                    fontFamily = AppTypography.fontFamily,
                    fontWeight = AppTypography.semiBold,
                    color = AppColors.Gray,
                    letterSpacing = 6.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }

        item {
            PodiumSection(podium = uiState.podium)
        }

        if (uiState.others.isNotEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(AppColors.GraySubtle)
                )
            }
            item {
                Text(
                    text = "CLASSEMENT",
                    fontSize = AppTypography.labelSize,
                    fontFamily = AppTypography.fontFamily,
                    fontWeight = AppTypography.semiBold,
                    color = AppColors.Gray,
                    letterSpacing = 3.sp,
                )
            }
            itemsIndexed(uiState.others) { _, entry ->
                OtherRunnerRow(entry = entry)
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun FinishHeader(onBack: () -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(CoreR.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(44.dp),
            )

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = EVENT_NAME,
                    fontSize = AppTypography.bodyLargeSize,
                    fontFamily = AppTypography.titleFontFamily,
                    fontWeight = AppTypography.bold,
                    color = AppColors.Yellow,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = EVENT_SUBTITLE,
                    fontSize = AppTypography.labelSize,
                    fontFamily = AppTypography.fontFamily,
                    color = AppColors.Yellow,
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Button(
                onClick = onBack,
                colors = ButtonDefaults.colors(
                    containerColor = AppColors.SurfaceMid,
                    focusedContainerColor = AppColors.Yellow,
                ),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Retour",
                    tint = AppColors.GraySubtle,
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .height(2.dp)
                .background(AppColors.Yellow),
        )
    }
}

@Composable
private fun PodiumSection(podium: List<PodiumEntryUiModel>) {
    // Arrange as 2nd - 1st - 3rd (classic podium order)
    val first = podium.getOrNull(0)
    val second = podium.getOrNull(1)
    val third = podium.getOrNull(2)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.Bottom,
    ) {
        // 2nd place – left
        PodiumSlot(
            entry = second,
            medalColor = Silver,
            medalEmoji = "🥈",
            photoSize = 72.dp,
            pedestalHeight = 90.dp,
            animDelay = 200,
            modifier = Modifier.weight(1f),
        )

        // 1st place – center (tallest)
        PodiumSlot(
            entry = first,
            medalColor = Gold,
            medalEmoji = "🥇",
            photoSize = 96.dp,
            pedestalHeight = 130.dp,
            animDelay = 0,
            modifier = Modifier.weight(1f),
        )

        // 3rd place – right
        PodiumSlot(
            entry = third,
            medalColor = Bronze,
            medalEmoji = "🥉",
            photoSize = 64.dp,
            pedestalHeight = 65.dp,
            animDelay = 350,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun PodiumSlot(
    entry: PodiumEntryUiModel?,
    medalColor: Color,
    medalEmoji: String,
    photoSize: Dp,
    pedestalHeight: Dp,
    animDelay: Int,
    modifier: Modifier = Modifier,
) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(entry) {
        if (entry == null) return@LaunchedEffect
        delay(animDelay.toLong())
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        )
    }
    LaunchedEffect(entry) {
        if (entry == null) return@LaunchedEffect
        delay(animDelay.toLong())
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400),
        )
    }

    Column(
        modifier = modifier
            .scale(scale.value)
            .alpha(alpha.value),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ) {
        if (entry != null) {
            // Medal emoji
            Text(
                text = medalEmoji,
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Runner photo
            Box(
                modifier = Modifier
                    .size(photoSize)
                    .clip(CircleShape)
                    .border(width = 3.dp, color = medalColor, shape = CircleShape),
            ) {
                Image(
                    painter = painterResource(entry.runner.photoResId),
                    contentDescription = entry.runner.firstName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Runner name
            Text(
                text = entry.runner.firstName.uppercase(),
                fontSize = AppTypography.bodySmallSize,
                fontFamily = AppTypography.fontFamily,
                fontWeight = AppTypography.bold,
                color = medalColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
            )

            // Laps count
            Text(
                text = "${entry.completedLaps} tour${if (entry.completedLaps > 1) "s" else ""}",
                fontSize = AppTypography.labelSize,
                fontFamily = AppTypography.fontFamily,
                fontWeight = AppTypography.semiBold,
                color = AppColors.WhiteDim,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(4.dp))
        }

        // Pedestal
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(pedestalHeight)
                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            medalColor.copy(alpha = 0.35f),
                            medalColor.copy(alpha = 0.15f),
                        )
                    )
                )
                .border(
                    width = 2.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(medalColor, medalColor.copy(alpha = 0.4f))
                    ),
                    shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (entry != null) {
                Text(
                    text = "#${entry.rank}",
                    fontSize = AppTypography.bodyLargeSize,
                    fontFamily = AppTypography.titleFontFamily,
                    fontWeight = AppTypography.bold,
                    color = medalColor,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun OtherRunnerRow(entry: PodiumEntryUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.SurfaceDark, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "#${entry.rank}",
            fontSize = AppTypography.bodyMediumSize,
            fontFamily = AppTypography.fontFamily,
            fontWeight = AppTypography.bold,
            color = AppColors.Gray,
            modifier = Modifier.width(48.dp),
            textAlign = TextAlign.Center,
        )

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.dp, AppColors.GraySubtle, CircleShape),
        ) {
            Image(
                painter = painterResource(entry.runner.photoResId),
                contentDescription = entry.runner.firstName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }

        Text(
            text = entry.runner.firstName,
            fontSize = AppTypography.bodySmallSize,
            fontFamily = AppTypography.fontFamily,
            fontWeight = AppTypography.semiBold,
            color = AppColors.White,
            modifier = Modifier.weight(1f),
        )

        Text(
            text = "${entry.completedLaps} tour${if (entry.completedLaps > 1) "s" else ""}",
            fontSize = AppTypography.labelSize,
            fontFamily = AppTypography.fontFamily,
            color = AppColors.WhiteDim,
        )
    }
}
