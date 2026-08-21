package org.tavioribeiro.griot.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val NewsreaderFamily = FontFamily.Serif
private val InterFamily = FontFamily.SansSerif
private val GeistMonoFamily = FontFamily.Monospace

@Immutable
data class GriotTypography(
    val display: TextStyle = TextStyle(
        fontFamily = NewsreaderFamily,
        fontWeight = FontWeight.Light,
        fontSize = 56.sp,
        lineHeight = 60.sp,
        letterSpacing = (-0.5).sp,
    ),
    val h1: TextStyle = TextStyle(
        fontFamily = NewsreaderFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.25).sp,
    ),
    val h2: TextStyle = TextStyle(
        fontFamily = NewsreaderFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp,
        lineHeight = 30.sp,
    ),
    val h3: TextStyle = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    ),
    val subtitle: TextStyle = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
    ),
    val body: TextStyle = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
    ),
    val body2: TextStyle = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    val label: TextStyle = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.2.sp,
    ),
    val numeric: TextStyle = TextStyle(
        fontFamily = GeistMonoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
    ),
    val caption: TextStyle = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.3.sp,
    ),
)

val GriotTypographyDefaults = GriotTypography()

internal val LocalGriotTypography = staticCompositionLocalOf { GriotTypographyDefaults }
