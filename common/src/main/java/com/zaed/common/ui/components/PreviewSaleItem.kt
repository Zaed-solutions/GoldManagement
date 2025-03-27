package com.zaed.common.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.sale.Discount
import com.zaed.common.data.model.sale.DiscountType
import com.zaed.common.data.model.sale.Product
import com.zaed.common.ui.util.toMoneyFormat

@Composable
fun PreviewSaleItem(
    modifier: Modifier = Modifier,
    product: Product,
    onShowProductDetails: (Product) -> Unit = {}
) {
    Log.d("PreviewSaleContentIN", "PreviewSaleContent: $product")
    val name = product.name.ifEmpty {
        "${product.grams} g"
    }
    Surface(
        onClick = { onShowProductDetails(product) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleMedium,
                    text = stringResource(
                        R.string.x,
                        product.quantity,
                        product.priceForItem
                    ) + if (product.laborCost > 0) " + ${product.totalLaborCost.toMoneyFormat(2)}" else "",
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {

                val validDiscount =
                    product.discountAmount > 0 && product.discount.type != DiscountType.NONE
                Text(
                    text = product.totalPriceBeforeDiscount.toMoneyFormat(
                        2
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium.copy(
                        textDecoration = if (validDiscount) TextDecoration.LineThrough else TextDecoration.None
                    ),

                    )
                if (validDiscount) {
                    Text(
                        text = product.totalPriceAfterDiscount.toMoneyFormat(
                            2
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "(${product.discountAsStr})",
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

            }

            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )

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
            discount = Discount(
                type = DiscountType.PERCENTAGE,
                value = 10.0
            )
        )
    )

}