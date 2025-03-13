package com.zaed.common.ui.suppliers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.supplier.Supplier
import com.zaed.common.ui.components.MoreDropDownMenu
import com.zaed.common.ui.components.MoreDropdownItem

@Composable
fun SupplierItem(
    modifier: Modifier = Modifier,
    supplier: Supplier,
    onClick: () -> Unit,
    isEditable: Boolean,
    onEdit: () -> Unit,
    isDeletable: Boolean,
    onDelete: () -> Unit,
) {
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error
    val moreOptions = remember(isEditable, isDeletable) {
        val list = mutableListOf<MoreDropdownItem>()
        if (isEditable) {
            list.add(
                MoreDropdownItem(
                    onClick = onEdit,
                    icon = Icons.Default.Edit,
                    title = context.getString(R.string.edit),
                    tint = primaryColor,
                )
            )
        }
        if (isDeletable) {
            list.add(
                MoreDropdownItem(
                    onClick = onDelete,
                    icon = Icons.Default.Delete,
                    title = context.getString(R.string.delete),
                    tint = errorColor,
                )
            )
        }
        list
    }
    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = { onClick() },
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Column {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSecondary,
                            shape = CircleShape
                        )
                        .padding(8.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = supplier.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
                    )
                    supplier.phone.let {
                        if (it.isNotBlank()) {
                            Text(
                                text = "+212-$it",
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                            )
                        }
                    }
                }

                if (moreOptions.isNotEmpty()) {
                    MoreDropDownMenu(
                        modifier = Modifier.padding(start = 8.dp),
                        items = moreOptions
                    )
                } else {
                    Icon(
                        modifier = Modifier.padding(start = 8.dp),
                        imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                        contentDescription = null
                    )
                }
            }
            HorizontalDivider(thickness = 1.dp)
        }
    }

}