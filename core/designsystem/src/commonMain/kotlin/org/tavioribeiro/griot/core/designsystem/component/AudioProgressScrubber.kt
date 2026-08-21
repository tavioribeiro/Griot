package org.tavioribeiro.griot.core.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import org.tavioribeiro.griot.core.designsystem.theme.GriotTheme

private fun formatMs(ms: Long): String {
    val totalSec = (ms / 1000).coerceAtLeast(0)
    val m = totalSec / 60
    val s = totalSec % 60
    return "${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}"
}

@Composable
fun AudioProgressScrubber(
    positionMs: Long,
    durationMs: Long,
    isSeeking: Boolean,
    onSeekStart: () -> Unit,
    onSeek: (Long) -> Unit,
    onSeekEnd: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = GriotTheme.colors
    val typography = GriotTheme.typography
    val shapes = GriotTheme.shapes
    val safeDuration = durationMs.coerceAtLeast(1L)
    val progress = (positionMs.toFloat() / safeDuration.toFloat()).coerceIn(0f, 1f)
    var trackWidthPx by remember { mutableStateOf(0f) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .onSizeChanged { trackWidthPx = it.width.toFloat() }
                .pointerInput(safeDuration) {
                    detectHorizontalDragGestures(
                        onDragStart = { onSeekStart() },
                        onDragEnd = { onSeekEnd(positionMs) },
                        onHorizontalDrag = { change, _ ->
                            change.consume()
                            val x = change.position.x.coerceIn(0f, trackWidthPx)
                            val newProgress = if (trackWidthPx > 0f) x / trackWidthPx else 0f
                            onSeek((newProgress * safeDuration).toLong())
                        },
                    )
                },
            contentAlignment = Alignment.Center,
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(shapes.pill),
            ) {
                val w = size.width
                val h = size.height
                drawRoundRect(
                    color = colors.border,
                    topLeft = Offset.Zero,
                    size = Size(w, h),
                    cornerRadius = CornerRadius(h / 2, h / 2),
                )
                drawRoundRect(
                    color = colors.accent,
                    topLeft = Offset.Zero,
                    size = Size(w * progress, h),
                    cornerRadius = CornerRadius(h / 2, h / 2),
                )
            }
            val knobSize = if (isSeeking) 24.dp else 20.dp
            val knobOffset = with(LocalDensity.current) {
                val knobPx = knobSize.toPx()
                val x = (trackWidthPx * progress - knobPx / 2).coerceIn(0f, (trackWidthPx - knobPx).coerceAtLeast(0f))
                x.toDp()
            }
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = knobOffset)
                    .size(knobSize)
                    .clip(shapes.pill)
                    .background(colors.onAccent)
                    .then(
                        if (isSeeking) Modifier.border(2.dp, colors.focusRing, shapes.pill) else Modifier
                    ),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = formatMs(positionMs),
                style = typography.numeric,
                color = if (isSeeking) colors.accent else colors.textSecondary,
            )
            Text(
                text = "-${formatMs((durationMs - positionMs).coerceAtLeast(0))}",
                style = typography.numeric,
                color = if (isSeeking) colors.accent else colors.textSecondary,
            )
        }
    }
}
