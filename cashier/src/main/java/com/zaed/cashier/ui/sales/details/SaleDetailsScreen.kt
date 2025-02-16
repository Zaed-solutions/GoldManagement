package com.zaed.cashier.ui.sales.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.cashier.R
import com.zaed.cashier.ui.theme.CashierAppTheme
import com.zaed.common.data.model.Category
import com.zaed.common.data.model.Product
import com.zaed.common.data.model.StoreSale
import com.zaed.common.ui.util.toMoneyFormat
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@Composable
fun SaleDetailsScreen(
    viewModel: SaleDetailsViewModel = koinViewModel(),
    saleId: String,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(true) {
        viewModel.setSaleId(saleId)
    }
    SaleDetailsScreenContent(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                SaleDetailsUiAction.OnBack -> onBack()
                else -> viewModel.handleAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleDetailsScreenContent(
    uiState: SaleDetailsUiState,
    onAction: (SaleDetailsUiAction) -> Unit
) {
    var state by remember { mutableStateOf(0) }
    val titles = listOf(stringResource(R.string.preview), stringResource(R.string.history))
    Scaffold(
    ) {
        Column(Modifier.padding(it)) {
            PrimaryTabRow(selectedTabIndex = state) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = state == index,
                        onClick = { state = index },
                        text = { Text(text = title) }
                    )
                }
            }
            when (state) {
                0 -> SaleDetailsPreview(uiState = uiState, onAction = onAction)
                1 -> Text(text = stringResource(R.string.history))
            }
        }
    }
}

@Composable
fun SaleDetailsPreview(
    uiState: SaleDetailsUiState,
    onAction: (SaleDetailsUiAction) -> Unit
) {

    val sale = uiState.storeSale
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHighest
            )
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.store),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = sale.storeName,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                )

            }
        }
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Text(
                        text = sale.id,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    FilledTonalButton(
                        contentPadding = PaddingValues(0.dp),
                        onClick = {},
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                            contentColor = MaterialTheme.colorScheme.primary

                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = stringResource(R.string.cash),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                }
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.date),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = sale.createdAt.toString().take(10),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.to),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = sale.customerName,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                )

            }
            androidx.compose.animation.AnimatedVisibility(sale.customerPhoneNumber.isNotEmpty()) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.phone_number),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = sale.customerPhoneNumber,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                    )

                }
            }
            androidx.compose.animation.AnimatedVisibility(sale.customerEmail.isNotEmpty()) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.email),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = sale.customerEmail,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                    )

                }
            }

        }
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.employee),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = sale.employeeName,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                )

            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp, vertical = 8.dp
                ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row (
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ){
                    Text(
                        text = stringResource(R.string.product),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.grams),
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.price),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                sale.products.forEach { product ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text(
                            text = product.name,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = product.grams.toString(),
                            style = MaterialTheme.typography.titleMedium,
                        )

                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = product.price.toString(),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
                FilledTonalButton(
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {},
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.subtotal),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = sale.products.sumOf {it.grams* it.price }.toMoneyFormat(),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp, vertical = 8.dp
                ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.discount),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = sale.discount.value.toString(),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }


                FilledTonalButton(
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {},
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.total),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = sale.totalPrice.toMoneyFormat(),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier.padding(16.dp)
        ){
            Button(
                shape = RoundedCornerShape(8.dp),
                onClick = {},
                modifier = Modifier.weight(1f)
                ){
                Text(stringResource(R.string.share))
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                shape = RoundedCornerShape(8.dp),
                onClick = {},
                modifier = Modifier.weight(1f)
                ) {
                Text(stringResource(R.string.print))
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Print,
                    contentDescription = null
                )

            }
        }
    }

}


@Preview
@Composable
private fun SaleDetailsScreenContentPreview() {
    CashierAppTheme {
        SaleDetailsScreenContent(
            uiState = SaleDetailsUiState(
                storeSale = StoreSale(
                    id = "INV0001",
                    createdAt = Date(),
                    storeId = "123456789",
                    storeName = "Goldawy",
                    employeeName = "Mohamed aly",
                    employeeId = "123456789",
                    customerName = "Ahmed alaa",
                    customerPhoneNumber = "123456789",
                    customerEmail = "william.henry.store.com",
                    products = listOf(
                        Product(
                            id = "123456789",
                            name = "Product 1",
                            price = 100.0,
                            category = Category(
                                id = "123456789",
                                name = "Category Name",
                            ),
                            grams = 5,
                        ),
                        Product(
                            id = "123456789",
                            name = "Product 2",
                            price = 200.0,
                            category = Category(
                                id = "123456789",
                                name = "Category Name",
                            ),
                            grams = 10,
                        )
                    ),
                    discount = com.zaed.common.data.model.Discount(
                        type = com.zaed.common.data.model.DiscountType.AMOUNT,
                        value = 10.0,
                    ),
                ),

                ),
            onAction = {}
        )
    }
}
