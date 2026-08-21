package org.tavioribeiro.griot.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.tavioribeiro.griot.core.designsystem.theme.GriotTheme

@Composable
fun NoteCard(
    text: String,
    trackLabel: String,
    timestampLabel: String,
    onPlaySnippet: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
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
            .background(colors.surface)
            .border(1.dp, colors.border, shapes.lg)
            .padding(spacing.space4),
        verticalArrangement = Arrangement.spacedBy(spacing.space3),
    ) {
        Text(
            text = text,
            style = typography.body,
            color = colors.textPrimary,
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.space2),
        ) {
            Box(
                modifier = Modifier
                    .clip(shapes.pill)
                    .background(colors.background2)
                    .border(1.dp, colors.border, shapes.pill)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = "$trackLabel · $timestampLabel",
                    style = typography.caption,
                    color = colors.textSecondary,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.space2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .clip(shapes.pill)
                    .background(colors.accent)
                    .clickable(onClick = onPlaySnippet)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
            ) {
                Text(
                    text = "Ouvir trecho",
                    style = typography.label,
                    color = colors.onAccent,
                )
            }

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .clip(shapes.pill)
                        .clickable(onClick = onEdit)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                ) {
                    Text(
                        text = "Editar",
                        style = typography.label,
                        color = colors.textSecondary,
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(shapes.pill)
                        .clickable(onClick = onDelete)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                ) {
                    Text(
                        text = "Excluir",
                        style = typography.label,
                        color = colors.error,
                    )
                }
            }
        }
    }
}
