package com.zaed.common.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.customer.Account
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.getProductSalePayments
import com.zaed.common.data.model.supplier.Supplier
import com.zaed.common.ui.addpurchase.ProductType

@Composable
fun PaymentTypes(
    selectedAccount: Account,
    types: List<PaymentType> = getProductSalePayments(),
    onPaymentTypeSelected: (PaymentType) -> Unit = {}
) {
    LazyColumn {
        items(types) { paymentType ->
            val enabled = if(paymentType == PaymentType.CHEQUE){
                selectedAccount.id.isNotBlank() && (selectedAccount is Supplier || (selectedAccount as? WholeSaleCustomer)?.payWithCheques?:false)
            } else{
                paymentType in listOf(PaymentType.CASH) || selectedAccount.id.isNotBlank()
            }
            Surface(
                onClick = { onPaymentTypeSelected(paymentType) },
                enabled = enabled,
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                    ) {
                        Icon(
                            painter = painterResource(paymentType.iconRes),
                            modifier = Modifier
                                .size(36.dp)
                                .padding(8.dp),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                    }
                    Text(
                        text = stringResource(paymentType.titleRes),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


@Composable
fun ProductTypes(
    onProductTypeSelected: (ProductType) -> Unit = {}
) {
    Surface (
        modifier = Modifier.padding(8.dp),
        border = BorderStroke(2.dp,MaterialTheme.colorScheme.primary),
        shape = MaterialTheme.shapes.medium
    ){
        LazyColumn(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            items(ProductType.entries) { productType ->
                Surface(
                    onClick = { onProductTypeSelected(productType) },
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            Modifier
                                .padding(16.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                        ) {
                            Icon(
                                painter = painterResource(productType.iconRes),
                                modifier = Modifier
                                    .size(64.dp)
                                    .padding(8.dp),
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null
                            )
                        }
                        Text(
                            text = stringResource(productType.titleRes),
                            style = MaterialTheme.typography.headlineMedium,
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun PaymentTypes(
    onPaymentTypeSelected: (Boolean) -> Unit = {}
) {
    Surface (
        modifier = Modifier.padding(8.dp),
        border = BorderStroke(2.dp,MaterialTheme.colorScheme.primary),
        shape = MaterialTheme.shapes.medium
    ){
        LazyColumn(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            item{
                Surface(
                    onClick = { onPaymentTypeSelected(true) },
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            Modifier
                                .padding(16.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_coins),
                                modifier = Modifier
                                    .size(64.dp)
                                    .padding(8.dp),
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null
                            )
                        }
                        Text(
                            text = stringResource(R.string.pay_with_money),
                            style = MaterialTheme.typography.headlineMedium,
                        )
                    }
                    HorizontalDivider()
                }
            }
            item{
                Surface(
                    onClick = { onPaymentTypeSelected(false) },
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            Modifier
                                .padding(16.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_gold),
                                modifier = Modifier
                                    .size(64.dp)
                                    .padding(8.dp),
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null
                            )
                        }
                        Text(
                            text = stringResource(R.string.pay_with_gold),
                            style = MaterialTheme.typography.headlineMedium,
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}