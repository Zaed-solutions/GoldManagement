package com.zaed.distributor.ui.productsaledetails.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.zaed.common.ui.components.MoreDropDownMenu
import com.zaed.common.ui.components.MoreDropdownItem
import com.zaed.common.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSaleDetailsTopBar(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error
    val items = remember {
        listOf(
            MoreDropdownItem(
                title = context.getString(R.string.edit),
                icon = Icons.Default.Edit,
                onClick = onEditClicked,
                tint = primaryColor
            ),
            MoreDropdownItem(
                title = context.getString(R.string.delete),
                icon = Icons.Default.Delete,
                onClick = onDeleteClicked,
                tint = errorColor
            )
        )
    }
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.sale_details),
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    onBackClicked()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            MoreDropDownMenu(
                items = items
            )
        }
    )

}