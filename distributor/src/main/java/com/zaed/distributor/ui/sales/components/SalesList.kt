package com.zaed.distributor.ui.sales.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.WholesaleGoldSale
import com.zaed.common.data.model.WholesaleProductSale
import com.zaed.common.data.model.WholesaleSale
import com.zaed.common.ui.components.ListWithLoading
import com.zaed.common.ui.components.SwipeToEditOrDeleteContainer

@Composable
fun SalesList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    sales: List<WholesaleSale>,
    onSaleClicked: (id: String, isProduct: Boolean) -> Unit,
    onDeleteSale: (id: String, isProduct: Boolean) -> Unit,
    onEditSale: (id: String, isProduct: Boolean) -> Unit
) {
    ListWithLoading(
        isLoading = isLoading
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = sales,
                key = { it.id }
            ) { sale ->
                when(sale){
                    is WholesaleProductSale -> {
                        SwipeToEditOrDeleteContainer(
                            modifier = Modifier.animateItem(),
                            onDelete = {
                                onDeleteSale(sale.id, true)
                            },
                            isEditEnabled = true,
                            onEdit = {
                                onEditSale(sale.id, true)
                            }
                        ) {
                            ProductSaleItem(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                sale = sale,
                                onSaleClicked = { onSaleClicked(sale.id, true) }
                            )
                        }
                    }
                    is WholesaleGoldSale -> {
                        SwipeToEditOrDeleteContainer(
                            modifier = Modifier.animateItem(),
                            onDelete = {
                                onDeleteSale(sale.id, false)
                            },
                            isEditEnabled = true,
                            onEdit = {
                                onEditSale(sale.id, false)
                            }
                        ) {
                            GoldSaleItem(
                                sale = sale,
                                onSaleClicked = {
                                    onSaleClicked(sale.id, false)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

