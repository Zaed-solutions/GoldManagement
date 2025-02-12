package com.zaed.common.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailRow(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    onClick: (() -> Unit) = {},
    onLongClick: (() -> Unit) = {},
    isDividerVisible: Boolean = true,
    isValueSingleLine: Boolean = true,
    textAlign: TextAlign = TextAlign.End,
    trailingContent: @Composable (() -> Unit)? = null
) {
    AnimatedVisibility(value.isNotBlank()) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = if (trailingContent == null) Alignment.Top else Alignment.CenterVertically,
            ) {
                Text(
                    text = label,
                    style = style,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )

                Text(
                    text = value,
                    style = style,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = if (isValueSingleLine) 1 else 5,
                    textAlign = textAlign,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 16.dp).weight(1f)
                        .combinedClickable(
                            onClick = { onClick() },
                            onLongClick = { onLongClick() }
                        )
                )
                trailingContent?.let {
                    it()
                }
            }
            if (isDividerVisible) {
                HorizontalDivider(
                    thickness = 1.dp,
                )
            }
        }
    }
}