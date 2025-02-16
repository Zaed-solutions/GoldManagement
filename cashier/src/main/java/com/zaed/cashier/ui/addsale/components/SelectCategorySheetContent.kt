package com.zaed.cashier.ui.addsale.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.cashier.R
import com.zaed.common.data.model.Category
import com.zaed.common.data.model.Product
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCategorySheetContent(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    onAddProduct: (Product) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val filteredCategories by remember(query) {
        mutableStateOf(categories.filter { it.name.contains(query, ignoreCase = true) })
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.select_category),
            style = MaterialTheme.typography.headlineMedium
        )
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
            },
            label = {
                Text(
                    text = stringResource(R.string.search)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search"
                )
            },
            shape = MaterialTheme.shapes.small,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredCategories) { category ->
                CategoryItem(
                    category = category,
                    onClick = {
                        onAddProduct(
                            Product(
                                id = UUID.randomUUID().toString(),
                                categoryId = category.id,
                                minPrice = category.minimumPrice,
                                gramPrice = category.minimumPrice
                            )
                        )
                    }
                )
            }
        }
    }
}



@Composable
private fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color.Transparent,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
