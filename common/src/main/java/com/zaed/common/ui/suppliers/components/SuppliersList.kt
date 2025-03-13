package com.zaed.common.ui.suppliers.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.supplier.Supplier
import com.zaed.common.ui.components.ListWithLoading

@Composable
fun SuppliersList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    suppliers: List<Supplier>,
    onSupplierClicked: (String) -> Unit,
    isEditable: Boolean,
    onEditSupplier: (Supplier) -> Unit,
    isDeletable: Boolean,
    onDeleteSupplier: (Supplier) -> Unit
){
    ListWithLoading(
        modifier = modifier,
        isLoading = isLoading
    ) {
        LazyColumn (
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ){
            items(
                items = suppliers,
                key = { it.id }
            ){supplier ->
                SupplierItem(
                    modifier = Modifier.animateItem(),
                    supplier = supplier,
                    onClick = { onSupplierClicked(supplier.id) },
                    isEditable = isEditable,
                    onEdit = { onEditSupplier(supplier) },
                    isDeletable = isDeletable,
                    onDelete = { onDeleteSupplier(supplier) }
                )
            }
        }
    }

}