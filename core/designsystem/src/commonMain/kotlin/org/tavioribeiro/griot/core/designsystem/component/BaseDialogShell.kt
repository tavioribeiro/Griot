package org.tavioribeiro.griot.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.tavioribeiro.griot.core.designsystem.theme.GriotTheme

@Composable
fun BaseDialogShell(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    primaryLabel: String? = null,
    onPrimary: (() -> Unit)? = null,
    secondaryLabel: String? = null,
    onSecondary: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val colors = GriotTheme.colors
    val typography = GriotTheme.typography
    val spacing = GriotTheme.spacing
    val shapes = GriotTheme.shapes

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .widthIn(min = 340.dp, max = 560.dp)
                .clip(shapes.xl)
                .background(colors.surface),
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(spacing.space5),
                verticalArrangement = Arrangement.spacedBy(spacing.space4),
            ) {
                Text(
                    text = title,
                    style = typography.h2,
                    color = colors.textPrimary,
                )

                content()

                if (primaryLabel != null || secondaryLabel != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (secondaryLabel != null) {
                            Box(
                                modifier = Modifier
                                    .clip(shapes.md)
                                    .clickable { onSecondary?.invoke() }
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                            ) {
                                Text(
                                    text = secondaryLabel,
                                    style = typography.label,
                                    color = colors.textSecondary,
                                )
                            }
                        }
                        if (primaryLabel != null) {
                            Box(
                                modifier = Modifier
                                    .clip(shapes.md)
                                    .background(colors.accent)
                                    .clickable { onPrimary?.invoke() }
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                            ) {
                                Text(
                                    text = primaryLabel,
                                    style = typography.label,
                                    color = colors.onAccent,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
