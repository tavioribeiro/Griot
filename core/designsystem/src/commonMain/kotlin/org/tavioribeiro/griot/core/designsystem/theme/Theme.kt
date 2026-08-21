package org.tavioribeiro.griot.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

private fun GriotColors.toLightMaterialScheme() = lightColorScheme(
    primary = accent,
    onPrimary = onAccent,
    primaryContainer = accentSoft,
    onPrimaryContainer = textPrimary,
    background = background,
    onBackground = textPrimary,
    surface = surface,
    onSurface = textPrimary,
    surfaceVariant = background2,
    onSurfaceVariant = textSecondary,
    outline = border,
    outlineVariant = borderStrong,
    error = error,
    onError = onAccent,
    tertiary = success,
    secondary = warning,
)

private fun GriotColors.toDarkMaterialScheme() = darkColorScheme(
    primary = accent,
    onPrimary = onAccent,
    primaryContainer = accentSoft,
    onPrimaryContainer = textPrimary,
    background = background,
    onBackground = textPrimary,
    surface = surface,
    onSurface = textPrimary,
    surfaceVariant = background2,
    onSurfaceVariant = textSecondary,
    outline = border,
    outlineVariant = borderStrong,
    error = error,
    onError = onAccent,
    tertiary = success,
    secondary = warning,
)

object GriotTheme {
    val colors: GriotColors
        @Composable @ReadOnlyComposable get() = LocalGriotColors.current

    val typography: GriotTypography
        @Composable @ReadOnlyComposable get() = LocalGriotTypography.current

    val spacing: GriotSpacing
        @Composable @ReadOnlyComposable get() = LocalGriotSpacing.current

    val shapes: GriotShapes
        @Composable @ReadOnlyComposable get() = LocalGriotShapes.current
}

@Composable
fun GriotTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: GriotColors = if (darkTheme) GriotDarkColors else GriotLightColors,
    typography: GriotTypography = GriotTypographyDefaults,
    spacing: GriotSpacing = GriotSpacingDefaults,
    shapes: GriotShapes = GriotShapesDefaults,
    content: @Composable () -> Unit,
) {
    val materialScheme = if (colors.isLight) colors.toLightMaterialScheme() else colors.toDarkMaterialScheme()

    CompositionLocalProvider(
        LocalGriotColors provides colors,
        LocalGriotTypography provides typography,
        LocalGriotSpacing provides spacing,
        LocalGriotShapes provides shapes,
    ) {
        MaterialTheme(
            colorScheme = materialScheme,
            typography = Typography(),
            content = content,
        )
    }
}
