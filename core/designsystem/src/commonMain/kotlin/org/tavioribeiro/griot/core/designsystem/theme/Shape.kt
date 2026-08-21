package org.tavioribeiro.griot.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

@Immutable
data class GriotShapes(
    val sm: RoundedCornerShape = RoundedCornerShape(5.dp),
    val md: RoundedCornerShape = RoundedCornerShape(10.dp),
    val lg: RoundedCornerShape = RoundedCornerShape(16.dp),
    val xl: RoundedCornerShape = RoundedCornerShape(24.dp),
    val pill: RoundedCornerShape = RoundedCornerShape(999.dp),
)

val GriotShapesDefaults = GriotShapes()

internal val LocalGriotShapes = staticCompositionLocalOf { GriotShapesDefaults }
