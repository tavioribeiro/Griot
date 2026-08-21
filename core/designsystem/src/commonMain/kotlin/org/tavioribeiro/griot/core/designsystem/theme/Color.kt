package org.tavioribeiro.griot.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class GriotColors(
    val background: Color,
    val background2: Color,
    val surface: Color,
    val surface2: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val border: Color,
    val borderStrong: Color,
    val accent: Color,
    val accentHover: Color,
    val accentSoft: Color,
    val onAccent: Color,
    val focusRing: Color,
    val success: Color,
    val warning: Color,
    val error: Color,
    val scrim: Color,
    val isLight: Boolean,
)

val GriotLightColors = GriotColors(
    background = Color(0xFFF5F2E9),
    background2 = Color(0xFFECE7DA),
    surface = Color(0xFFFCFAF4),
    surface2 = Color(0xFFFFFFFF),
    textPrimary = Color(0xFF2D2926),
    textSecondary = Color(0xFF5E5954),
    textTertiary = Color(0xFF8B857B),
    border = Color(0xFFDCD8CB),
    borderStrong = Color(0xFFC5BFB0),
    accent = Color(0xFF7D6B3D),
    accentHover = Color(0xFF6C5B32),
    accentSoft = Color(0xFFEDE6D3),
    onAccent = Color(0xFFFAF6EC),
    focusRing = Color(0xFFB9A26B),
    success = Color(0xFF3F6B4F),
    warning = Color(0xFF8C6A2E),
    error = Color(0xFFA04548),
    scrim = Color(0x66000000),
    isLight = true,
)

val GriotDarkColors = GriotColors(
    background = Color(0xFF1B1916),
    background2 = Color(0xFF24211D),
    surface = Color(0xFF2D2926),
    surface2 = Color(0xFF38332D),
    textPrimary = Color(0xFFF0ECE2),
    textSecondary = Color(0xFFA9A196),
    textTertiary = Color(0xFF6E675E),
    border = Color(0xFF3D3831),
    borderStrong = Color(0xFF4D473E),
    accent = Color(0xFFC6AE7C),
    accentHover = Color(0xFFD3BD8E),
    accentSoft = Color(0xFF3F3A2E),
    onAccent = Color(0xFF221F1A),
    focusRing = Color(0xFF8F845F),
    success = Color(0xFF7CA985),
    warning = Color(0xFFC79A59),
    error = Color(0xFFC4767A),
    scrim = Color(0x99000000),
    isLight = false,
)

internal val LocalGriotColors = staticCompositionLocalOf { GriotLightColors }
