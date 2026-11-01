package com.athimue.backyard.core.theme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Glow
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.athimue.backyard.core.theme.AppColors
import com.athimue.backyard.core.theme.AppTypography

/**
 * Button roles of the app. Same palette everywhere, so a colour always means the same thing:
 * yellow = main action, green = start, red = destructive, dark = everything else.
 */
enum class BackyardButtonStyle { Primary, Start, Secondary, Danger }

private data class ButtonPalette(
    val container: Color,
    val content: Color,
    val focusedContainer: Color,
    val focusedContent: Color,
    val idleBorder: Color,
)

private fun BackyardButtonStyle.palette(armed: Boolean = false): ButtonPalette = when (this) {
    BackyardButtonStyle.Primary -> ButtonPalette(
        container = AppColors.Yellow, content = AppColors.Black,
        focusedContainer = AppColors.Yellow, focusedContent = AppColors.Black,
        idleBorder = Color.Transparent,
    )
    BackyardButtonStyle.Start -> ButtonPalette(
        container = AppColors.GreenFilled, content = AppColors.White,
        focusedContainer = AppColors.GreenFilledBorder, focusedContent = AppColors.White,
        idleBorder = AppColors.GreenFilledBorder.copy(alpha = 0.6f),
    )
    // Focus = yellow with BLACK text (white on yellow was hard to read from the sofa)
    BackyardButtonStyle.Secondary -> ButtonPalette(
        container = AppColors.SurfaceMid, content = AppColors.White,
        focusedContainer = AppColors.Yellow, focusedContent = AppColors.Black,
        idleBorder = AppColors.GraySubtle,
    )
    BackyardButtonStyle.Danger -> ButtonPalette(
        container = if (armed) AppColors.RedFilledBorder else AppColors.RedFilled,
        content = AppColors.White,
        focusedContainer = AppColors.RedFilledBorder, focusedContent = AppColors.White,
        idleBorder = AppColors.RedFilledBorder.copy(alpha = 0.6f),
    )
}

/**
 * The app's button: rounded, icon + label, and a clear focus state for the D-pad
 * (slight zoom, white ring, soft coloured halo).
 */
@Composable
fun BackyardButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    style: BackyardButtonStyle = BackyardButtonStyle.Secondary,
    onLongClick: (() -> Unit)? = null,
    fontSize: TextUnit = AppTypography.bodySmallSize,
    armed: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(horizontal = 28.dp, vertical = 14.dp),
) {
    val shape = RoundedCornerShape(14.dp)
    BackyardButtonFrame(
        onClick = onClick,
        onLongClick = onLongClick,
        modifier = modifier,
        palette = style.palette(armed),
        shape = shape,
        contentPadding = contentPadding,
    ) { contentColor ->
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(12.dp))
        }
        Text(
            text = text,
            color = contentColor,
            fontSize = fontSize,
            fontFamily = AppTypography.fontFamily,
            fontWeight = AppTypography.bold,
            maxLines = 1,
        )
    }
}

/** Round icon-only variant (back, settings, arrows…). */
@Composable
fun BackyardIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: BackyardButtonStyle = BackyardButtonStyle.Secondary,
    onLongClick: (() -> Unit)? = null,
    iconSize: Dp = 24.dp,
    padding: Dp = 12.dp,
) {
    BackyardButtonFrame(
        onClick = onClick,
        onLongClick = onLongClick,
        modifier = modifier,
        palette = style.palette(),
        shape = CircleShape,
        contentPadding = PaddingValues(padding),
    ) { contentColor ->
        Icon(imageVector = icon, contentDescription = contentDescription, tint = contentColor, modifier = Modifier.size(iconSize))
    }
}

@Composable
private fun BackyardButtonFrame(
    onClick: () -> Unit,
    onLongClick: (() -> Unit)?,
    modifier: Modifier,
    palette: ButtonPalette,
    shape: Shape,
    contentPadding: PaddingValues,
    content: @Composable (contentColor: Color) -> Unit,
) {
    Button(
        onClick = onClick,
        onLongClick = onLongClick,
        modifier = modifier,
        shape = ButtonDefaults.shape(shape = shape),
        colors = ButtonDefaults.colors(
            containerColor = palette.container,
            contentColor = palette.content,
            focusedContainerColor = palette.focusedContainer,
            focusedContentColor = palette.focusedContent,
            pressedContainerColor = palette.focusedContainer,
            pressedContentColor = palette.focusedContent,
        ),
        border = ButtonDefaults.border(
            border = Border(BorderStroke(1.dp, palette.idleBorder), shape = shape),
            focusedBorder = Border(BorderStroke(2.dp, AppColors.White), shape = shape),
            pressedBorder = Border(BorderStroke(2.dp, AppColors.White), shape = shape),
        ),
        glow = ButtonDefaults.glow(
            focusedGlow = Glow(elevationColor = palette.focusedContainer.copy(alpha = 0.5f), elevation = 12.dp),
        ),
        scale = ButtonDefaults.scale(focusedScale = 1.06f, pressedScale = 1.02f),
        contentPadding = contentPadding,
    ) {
        // Content colour follows the focus state through LocalContentColor
        content(androidx.tv.material3.LocalContentColor.current)
    }
}
