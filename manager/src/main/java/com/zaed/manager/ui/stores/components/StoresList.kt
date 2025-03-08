package com.zaed.manager.ui.stores.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.store.Store
import com.zaed.common.ui.components.ListWithLoading
import com.zaed.common.ui.components.MoreDropDownMenu
import com.zaed.common.ui.components.MoreDropdownItem

@Composable
fun StoresList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    stores: List<Store>,
    onStoreClicked: (Store) -> Unit,
    onEditStore: (Store) -> Unit,
    onDeleteStore: (Store) -> Unit
) {
    ListWithLoading(
        modifier = modifier,
        isLoading = isLoading,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(stores) { store ->
                StoreItem(
                    store = store,
                    onStoreClicked = { onStoreClicked(store) },
                    onEdit = { onEditStore(store) },
                    onDelete = { onDeleteStore(store) }
                )
            }
        }
    }
}


@Composable
fun StoreItem(
    modifier: Modifier = Modifier,
    store: Store,
    onStoreClicked: () -> Unit,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Surface(
        modifier = modifier,
        onClick = onStoreClicked,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column (
                    modifier = Modifier.weight(1f)
                ){
                    Text(
                        text = store.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 8.dp)
                    )
                    Text(
                        text = store.location,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }
                MoreDropDownMenu(
                    items = listOf(
                        MoreDropdownItem(
                            onClick = { onEdit()},
                            title = stringResource(R.string.edit),
                            tint = MaterialTheme.colorScheme.primary,
                            icon = Icons.Default.Edit
                        ),
                        MoreDropdownItem(
                            onClick = { onDelete()},
                            title = stringResource(R.string.delete),
                            tint = MaterialTheme.colorScheme.error,
                            icon = Icons.Default.Delete
                        )
                    )
                )
            }
            HorizontalDivider(thickness = 1.dp)
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun ItemPreview() {
    StoreItem(
        store = Store(name = "Store 1", location = "Location 1"),
        onStoreClicked = {}
    )
}


@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun ListPreview() {
    StoresList(
        modifier = Modifier.padding(vertical = 24.dp),
        isLoading = false,
        stores = listOf(
            Store(name = "Store 1", location = "Location 1"),
            Store(name = "Store 2", location = "Location 2"),
            Store(name = "Store 3", location = "Location 3"),
            Store(name = "Store 4", location = "Location 4"),
            Store(name = "Store 5", location = "Location 5"),
            Store(name = "Store 6", location = "Location 6"),
            Store(name = "Store 7", location = "Location 7"),
            Store(name = "Store 8", location = "Location 8"),
            Store(name = "Store 9", location = "Location 9"),
            Store(name = "Store 10", location = "Location 10"),
        ),
        onStoreClicked = {},
        onEditStore = {},
        onDeleteStore = {}
    )
}