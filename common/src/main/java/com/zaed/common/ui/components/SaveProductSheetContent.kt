package com.zaed.common.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.sale.DiscountType
import com.zaed.common.data.model.sale.Product

@Composable
fun SaveProductSheetContent(
    modifier: Modifier = Modifier,
    isStoreSale: Boolean,
    initialProduct: Product,
    availableGrams : Double = Double.MAX_VALUE,
    onSaveProduct: (Product) -> Unit,
    deleteProduct: (Product) -> Unit
) {
    var product by remember { mutableStateOf(initialProduct) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.add_product) + " ${product.name}",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))
        ProductFieldsContent(
            isStoreSale = isStoreSale,
            availableGrams = availableGrams,
            product1 = product,
            onValueChange = { newProduct ->
                product = newProduct
            },
        )
        Spacer(modifier = Modifier.height(32.dp))
        //grams
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
                    .heightIn(min = 48.dp),
                shape = MaterialTheme.shapes.medium,
                onClick = {
                    onSaveProduct(product)
                },
                enabled = product.grams > 0.0 && product.gramPrice > 0.0 && product.quantity <= availableGrams
            ) {
                Text(
                    text = stringResource(R.string.save)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            FilledTonalButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
                    .heightIn(min = 48.dp),
                shape = MaterialTheme.shapes.medium,
                onClick = {
                    deleteProduct(product)
                },
            ) {
                Text(
                    text = stringResource(R.string.delete)
                )
            }
        }
    }
}

@Composable
fun ProductFieldsContent(
    isStoreSale: Boolean,
    product1: Product,
    availableGrams : Double = Double.MAX_VALUE,
    onValueChange: (Product) -> Unit={},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val focusRequester = remember { FocusRequester() }
        //grams
        if (isStoreSale) {
            TextInputTextField(
                value = product1.name,
                onValueChange = { value ->
                    onValueChange(product1.copy(name = value))
                },
                label = stringResource(R.string.product_name),
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                withBorder = true,
            )
        }
        NumberInputTextField(
            value = product1.grams,
            onValueChange = { value ->
                onValueChange(product1.copy(grams = value))
            },
            label = stringResource(R.string.grams),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            withBorder = true,
            isError =  product1.grams > availableGrams,
            errorMessage = R.string.quantity_cannot_be_zero,
            supportingText = availableGrams?.let { stringResource(R.string.available_stock_template, it) }?:""
        )
        //gram price
        NumberInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = product1.gramPrice,
            onValueChange = { value ->
                onValueChange(product1.copy(gramPrice = value))
            },
            label = stringResource(R.string.gram_price),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            shape = RoundedCornerShape(4.dp),
            withBorder = true,
        )
        if(isStoreSale) {
            //discount
            NumberInputTextField(
                value = product1.discount.value,
                onValueChange = { value ->
                    onValueChange(product1.copy(discount = product1.discount.copy(value = value)))
                },
                enabled = product1.discount.type != DiscountType.NONE,
                focusRequester = focusRequester,
                trailingIcon = {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 4.dp)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(4.dp)
                            ),
                    ) {
                        //  percentage
                        Surface(
                            onClick = {
                                onValueChange(product1.copy(discount = product1.discount.copy(type = DiscountType.PERCENTAGE)))
                                focusRequester.requestFocus()
                            },
                            color = if (product1.discount.type == DiscountType.PERCENTAGE) MaterialTheme.colorScheme.primaryContainer.copy(
                                alpha = 0.3f
                            ) else MaterialTheme.colorScheme.surfaceContainer,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Percent,
                                contentDescription = "Sale icon",
                                tint = if (product1.discount.type == DiscountType.PERCENTAGE) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        //value
                        Surface(
                            onClick = {
                                onValueChange(product1.copy(discount = product1.discount.copy(type = DiscountType.AMOUNT)))
                                focusRequester.requestFocus()
                            },
                            color = if (product1.discount.type == DiscountType.AMOUNT) MaterialTheme.colorScheme.primaryContainer.copy(
                                alpha = 0.3f
                            ) else MaterialTheme.colorScheme.surfaceContainer,
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .clip(RoundedCornerShape(4.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachMoney,
                                contentDescription = "Sale icon",
                                tint = if (product1.discount.type == DiscountType.AMOUNT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                },
                label = stringResource(R.string.discount),
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                withBorder = true,
            )
        }
        //quqntity
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.quantity),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.weight(1f))
            // -
            Surface(
                onClick = {
                    if (product1.quantity == 1) return@Surface
                    onValueChange(product1.copy(quantity = product1.quantity - 1))
                },
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .clip(RoundedCornerShape(4.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Sale icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }
            //quantity text
            Surface(
                Modifier
                    .padding(end = 4.dp)
                    .width(48.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(4.dp)
                    )
            ) {
                Text(
                    text = product1.quantity.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
            // +
            Surface(
                onClick = {
                    onValueChange(product1.copy(quantity = product1.quantity + 1))
                },
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                modifier = Modifier

                    .clip(RoundedCornerShape(4.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Sale icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun GoldFieldsContent(
    product1: Product,
    onValueChange: (Product) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val focusRequester = remember { FocusRequester() }

        NumberInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = product1.grams,
            onValueChange = { value ->
                onValueChange(product1.copy(grams = value))
            },
            label = stringResource(R.string.grams),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        )
        NumberInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = product1.gramPrice,
            onValueChange = { value ->
                onValueChange(product1.copy(gramPrice = value))
            },
            label = stringResource(R.string.gram_price),

            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        )
        NumberInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = product1.laborCost,
            onValueChange = { value ->
                onValueChange(product1.copy(laborCost = value))
            },
            label = stringResource(R.string.labor_cost),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        )

        //discount
        NumberInputTextField(
            value = product1.discount.value,
            onValueChange = { value ->
                onValueChange(product1.copy(discount = product1.discount.copy(value = value)))
            },
            enabled = product1.discount.type != DiscountType.NONE,
            focusRequester = focusRequester,
            trailingIcon = {
                Row(
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 4.dp)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(4.dp)
                        ),
                ) {
                    //  percentage
                    Surface(
                        onClick = {
                            onValueChange(product1.copy(discount = product1.discount.copy(type = DiscountType.PERCENTAGE)))
                            focusRequester.requestFocus()
                        },
                        color = if (product1.discount.type == DiscountType.PERCENTAGE) MaterialTheme.colorScheme.primaryContainer.copy(
                            alpha = 0.3f
                        ) else MaterialTheme.colorScheme.surfaceContainer,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Percent,
                            contentDescription = "Sale icon",
                            tint = if (product1.discount.type == DiscountType.PERCENTAGE) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    //value
                    Surface(
                        onClick = {
                            onValueChange(product1.copy(discount = product1.discount.copy(type = DiscountType.AMOUNT)))
                            focusRequester.requestFocus()
                        },
                        color = if (product1.discount.type == DiscountType.AMOUNT) MaterialTheme.colorScheme.primaryContainer.copy(
                            alpha = 0.3f
                        ) else MaterialTheme.colorScheme.surfaceContainer,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .clip(RoundedCornerShape(4.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = "Sale icon",
                            tint = if (product1.discount.type == DiscountType.AMOUNT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            },
            label = stringResource(R.string.discount),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            withBorder = true,
        )
        //quqntity
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.quantity),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.weight(1f))
            // -
            Surface(
                onClick = {
                    if (product1.quantity == 1) return@Surface
                    onValueChange(product1.copy(quantity = product1.quantity - 1))
                },
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .clip(RoundedCornerShape(4.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Sale icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }
            //quantity text
            Surface(
                Modifier
                    .padding(end = 4.dp)
                    .width(48.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(4.dp)
                    )
            ) {
                Text(
                    text = product1.quantity.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
            // +
            Surface(
                onClick = {
                    onValueChange(product1.copy(quantity = product1.quantity + 1))
                },
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                modifier = Modifier

                    .clip(RoundedCornerShape(4.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Sale icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Previewff() {
    ProductFieldsContent(
        isStoreSale = true,
        product1 = Product(),
    )
}