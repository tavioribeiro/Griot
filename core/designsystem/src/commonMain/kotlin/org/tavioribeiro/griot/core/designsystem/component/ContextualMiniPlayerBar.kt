package org.tavioribeiro.griot.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.tavioribeiro.griot.core.designsystem.theme.GriotTheme

@Composable
fun ContextualMiniPlayerBar(
    label: String,
    progress: Float,
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = GriotTheme.colors
    val typography = GriotTheme.typography
    val spacing = GriotTheme.spacing
    val shapes = GriotTheme.shapes

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shapes.lg)
            .background(colors.surface2)
            .border(1.dp, colors.border, shapes.lg)
            .padding(spacing.space3),
        verticalArrangement = Arrangement.spacedBy(spacing.space2),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = label,
                style = typography.caption,
                color = colors.textSecondary,
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(shapes.pill)
                    .clickable(onClick = onClose)
                    .background(colors.background2),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "✕",
                    style = typography.label,
                    color = colors.textSecondary,
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(shapes.pill)
                .background(colors.border),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                    .height(4.dp)
                    .clip(shapes.pill)
                    .background(colors.accent),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .clip(shapes.pill)
                    .background(colors.accent)
                    .clickable(onClick = onPlayPause)
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = if (isPlaying) "Pausar" else "Reproduzir",
                    style = typography.label,
                    color = colors.onAccent,
                )
            }
        }
    }
}
