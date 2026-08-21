package org.tavioribeiro.griot.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class GriotSpacing(
    val space1: Dp = 4.dp,
    val space2: Dp = 8.dp,
    val space3: Dp = 12.dp,
    val space4: Dp = 16.dp,
    val space5: Dp = 24.dp,
    val space6: Dp = 32.dp,
    val space7: Dp = 48.dp,
)

val GriotSpacingDefaults = GriotSpacing()

internal val LocalGriotSpacing = staticCompositionLocalOf { GriotSpacingDefaults }
