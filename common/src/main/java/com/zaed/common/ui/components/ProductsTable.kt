package com.zaed.common.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.sale.Product

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductsTable(
    modifier: Modifier = Modifier,
    products: List<Product>,
    isModifyEnabled: Boolean = true
) {
    Surface(
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),

            ),
        shape = MaterialTheme.shapes.medium
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header
            stickyHeader {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (products.firstOrNull()?.name?.isNotEmpty() == true) {
                            Text(
                                text = stringResource(R.string.product),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(5f)
                            )
                        }
                        Text(
                            text = stringResource(R.string.grams),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(3f),
                            textAlign = TextAlign.Center
                        )
                        if (products.firstOrNull()?.name?.isEmpty() == true) {
                            Text(
                                text = stringResource(R.string.labor_cost),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(5f),
                                textAlign = TextAlign.Center
                            )
                        }
                        Text(
                            text = stringResource(R.string.price),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(3f),
                            textAlign = TextAlign.End
                        )
                    }
                    HorizontalDivider()
                }
            }
            items(products) {
                when (isModifyEnabled) {
                    true -> {
                        ProductItem(
                            modifier = Modifier
                                .animateItem(),
                            product = it,
                        )
                    }

                    false -> {
                        ProductItem(
                            modifier = Modifier
                                .animateItem(),
                            product = it,
                        )
                    }
                }

            }
        }
    }
}

