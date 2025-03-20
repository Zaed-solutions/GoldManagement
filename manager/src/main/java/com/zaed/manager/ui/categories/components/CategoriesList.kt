package com.zaed.manager.ui.categories.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.ui.components.ListWithLoading
import com.zaed.common.data.model.category.CategoryWithInventory

@Composable
fun CategoriesList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    categories: List<CategoryWithInventory>,
    onEdit: (CategoryWithInventory) -> Unit,
    onDelete: (CategoryWithInventory) -> Unit
){
    ListWithLoading(
        modifier= modifier,
        isLoading = isLoading
    ) {
        LazyColumn (
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ){
            items(
                items = categories,
                key = { it.category.id }
            ){ category ->
                CategoryItem(
                    modifier = Modifier.animateItem(),
                    category = category.category,
                    availableStock = category.inventory.quantity,
                    onEdit = { onEdit(category) },
                    onDelete = { onDelete(category) }
                )
            }
        }
    }

}