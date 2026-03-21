package com.athimue.backyard.core.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

object AppTypography {
    val fontFamily = FontFamily.Monospace
    val titleFontFamily = FontFamily(Font(R.font.capsmall_clean))

    val titleSize: TextUnit = 52.sp
    val subtitleSize: TextUnit = 28.sp
    val bodyLargeSize: TextUnit = 36.sp
    val bodyMediumSize: TextUnit = 24.sp
    val bodySmallSize: TextUnit = 18.sp
    val labelSize: TextUnit = 14.sp
    val clockSize: TextUnit = 80.sp
    val chronoSize: TextUnit = 72.sp

    val bold = FontWeight.Bold
    val semiBold = FontWeight.SemiBold
    val normal = FontWeight.Normal
}
