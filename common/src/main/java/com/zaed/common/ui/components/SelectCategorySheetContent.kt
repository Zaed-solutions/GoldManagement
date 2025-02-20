package com.zaed.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
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
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.select_category),
            style = MaterialTheme.typography.headlineMedium
        )
        SearchBar(
            modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp),
            query = query,
            onQueryChanged = {
                query = it
            }
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp),
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
        color = Color.Transparent,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun Preview() {
        SelectCategorySheetContent(
            categories = listOf(
                Category(
                    id = "1",
                    name = "Category 1",
                    minimumPrice = 10.0
                ),
                Category(
                    id = "2",
                    name = "Category 2",
                    minimumPrice = 10.0
                ),
                Category(
                    id = "3",
                    name = "Category 3",
                    minimumPrice = 10.0
                ),
                Category(
                    id = "4",
                    name = "Category 4",
                    minimumPrice = 10.0
                )

            )
        ) { }
}