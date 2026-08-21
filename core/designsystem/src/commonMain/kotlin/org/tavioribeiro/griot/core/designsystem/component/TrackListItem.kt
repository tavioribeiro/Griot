package org.tavioribeiro.griot.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
fun TrackListItem(
    index: Int,
    title: String,
    durationLabel: String,
    isActive: Boolean,
    isDragging: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = GriotTheme.colors
    val typography = GriotTheme.typography
    val spacing = GriotTheme.spacing
    val shapes = GriotTheme.shapes

    val background = when {
        isDragging -> colors.accentSoft
        isActive -> colors.accentSoft
        else -> colors.surface
    }

    val borderColor = when {
        isDragging -> colors.borderStrong
        else -> colors.border
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shapes.md)
            .background(background)
            .border(1.dp, borderColor, shapes.md)
            .padding(horizontal = spacing.space4, vertical = spacing.space3),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.space3),
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(shapes.sm)
                .background(if (isActive) colors.accent else colors.background2)
                .border(1.dp, if (isActive) colors.accent else colors.border, shapes.sm),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = index.toString().padStart(2, '0'),
                style = typography.caption,
                color = if (isActive) colors.onAccent else colors.textSecondary,
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = title,
                style = typography.subtitle,
                color = if (isActive) colors.accent else colors.textPrimary,
                maxLines = 1,
            )
            if (isDragging) {
                Box(
                    modifier = Modifier
                        .clip(shapes.pill)
                        .background(colors.accent)
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                ) {
                    Text(
                        text = "Em arrasto",
                        style = typography.caption,
                        color = colors.onAccent,
                    )
                }
            }
        }

        Text(
            text = durationLabel,
            style = typography.numeric,
            color = if (isActive) colors.accent else colors.textSecondary,
        )

        if (isActive) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(shapes.pill)
                    .background(colors.accent),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "♪",
                    style = typography.caption,
                    color = colors.onAccent,
                )
            }
        }
    }
}
