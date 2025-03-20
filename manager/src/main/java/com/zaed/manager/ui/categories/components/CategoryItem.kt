package com.zaed.manager.ui.categories.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.category.Category
import com.zaed.common.R
import com.zaed.common.ui.components.MoreDropDownMenu
import com.zaed.common.ui.components.MoreDropdownItem

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    availableStock: Double,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Surface(
        color = Color.Transparent,
        onClick = { onEdit() },
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .padding(end = 4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))

            ) {
                Text(
                    text = category.name.first().toString(),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {

                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(
                        R.string.available_stock_template,
                        stringResource(
                            R.string.grams_placeholder,
                            availableStock
                        )
                    )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            MoreDropDownMenu(
                items = listOf(
                    MoreDropdownItem(
                        onClick = {
                            onEdit()
                        },
                        title = stringResource(R.string.edit),
                        tint = MaterialTheme.colorScheme.primary,
                        icon = Icons.Default.Edit
                    ),
                    MoreDropdownItem(
                        onClick = {
                            onDelete()
                        },
                        title = stringResource(R.string.delete),
                        tint = MaterialTheme.colorScheme.error,
                        icon = Icons.Default.Delete
                    )
                )
            )
        }
    }
}