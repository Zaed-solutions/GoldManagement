package com.zaed.manager.ui.manufacturerorders.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.manufacturerorder.ManufacturerOrder
import com.zaed.common.ui.components.ListWithLoading

@Composable
fun ManufacturerOrdersList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    orders: List<ManufacturerOrder>,
    onEdit: (ManufacturerOrder) -> Unit,
    onDelete: (ManufacturerOrder) -> Unit
){
    ListWithLoading(
        modifier = modifier,
        isLoading = isLoading
    ) {
        LazyColumn (
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
        ){
            items(
                items = orders,
                key = { it.id }
            ) { order ->
                ManufacturerOrderItem(
                    modifier = Modifier.animateItem(),
                    order = order,
                    onEdit = {
                        onEdit(order)
                    },
                    onDelete = {
                        onDelete(order)
                    }
                )
            }
        }
    }
}