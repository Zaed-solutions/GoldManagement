package com.zaed.distributor.ui.addproductsale.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.sale.DiscountType
import com.zaed.common.data.model.sale.Product
import com.zaed.common.ui.util.toMoneyFormat

@Composable
fun PreviewSaleItem(
    modifier: Modifier = Modifier,
    product: Product,
    onShowProductDetails: (Product) -> Unit = {}
) {
    Surface(
        onClick = { onShowProductDetails(product) },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 24.dp),
                    text = stringResource(
                        R.string.x,
                        product.quantity,
                        product.priceForItem
                    ),
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    modifier = Modifier.padding(end = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val validDiscount = product.discountAmount > 0 && product.discount.type != DiscountType.NONE
                    Text(
                        text = product.totalPriceBeforeDiscount.toMoneyFormat(
                            2
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge.copy(
                            textDecoration = if (validDiscount) TextDecoration.LineThrough else TextDecoration.None
                        ),

                    )
                    if(validDiscount) {
                        Text(
                            text = product.totalPriceAfterDiscount.toMoneyFormat(
                                2
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "(${product.discountAsStr})",
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSaleItemPreview() {
    PreviewSaleItem(
        product = Product(
            name = "Gold",
            quantity = 1,
            gramPrice = 100.0,
            grams = 36.0,
            discount = com.zaed.common.data.model.sale.Discount(
                type = com.zaed.common.data.model.sale.DiscountType.PERCENTAGE,
                value = 10.0
            )
        )
    )
    
}