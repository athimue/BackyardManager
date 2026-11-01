package com.athimue.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.athimue.backyard.core.theme.AppColors
import com.athimue.backyard.core.theme.AppTypography
import com.athimue.ui.model.RunnerUiModel

/**
 * Runner photo, or the runner's initial on a round badge when no photo exists
 * (runners added later no longer all get the same fallback photo).
 */
@Composable
internal fun RunnerAvatar(
    runner: RunnerUiModel,
    modifier: Modifier = Modifier,
) {
    val photo = runner.photoResId
    if (photo != null) {
        Image(
            painter = painterResource(photo),
            contentDescription = runner.firstName,
            contentScale = ContentScale.Crop,
            modifier = modifier,
        )
    } else {
        BoxWithConstraints(
            modifier = modifier.background(AppColors.SurfaceMid, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = runner.firstName.take(1).uppercase().ifEmpty { "?" },
                color = AppColors.Yellow,
                fontSize = (maxHeight.value * 0.5f).sp,
                fontFamily = AppTypography.fontFamily,
                fontWeight = AppTypography.bold,
                maxLines = 1,
            )
        }
    }
}
