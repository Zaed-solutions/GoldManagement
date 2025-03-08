package com.zaed.cashier.ui.saledetails.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zaed.cashier.ui.saledetails.SaleDetailsUiAction
import com.zaed.cashier.ui.saledetails.SaleDetailsUiState
import com.zaed.common.R
import com.zaed.common.data.model.sale.DiscountType
import com.zaed.common.ui.components.MultiOptionSwitch
import com.zaed.common.ui.components.PhoneNumberTextField
import com.zaed.common.ui.components.TextInputTextField
import com.zaed.common.ui.util.isValidPhoneNumber
import com.zaed.common.ui.util.toMoneyFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleDetailsPreview(
    uiState: SaleDetailsUiState,
    onAction: (SaleDetailsUiAction) -> Unit
) {
    var isShareBottomSheetIsVisible by remember { mutableStateOf(false) }
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
                    text = stringResource(R.string.store_colon),
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CR-" + sale.receiptNumber,
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
                    verticalAlignment = Alignment.CenterVertically
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
            androidx.compose.animation.AnimatedVisibility(sale.customerPhone.isNotEmpty()) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.phone_number_colon),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = sale.customerPhone,
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
                        text = stringResource(R.string.email_colon),
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
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
                        verticalAlignment = Alignment.CenterVertically
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
                            text = product.gramPrice.toString(),
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.subtotal),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = sale.products.sumOf { it.grams * it.gramPrice }.toMoneyFormat(),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }
        }
        AnimatedVisibility(sale.discount.type != DiscountType.NONE) {
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.discount),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = when (sale.discount.type) {
                                DiscountType.PERCENTAGE -> "${sale.discount.value}%"
                                else -> sale.discount.value.toMoneyFormat()
                            },
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
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.total),
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = sale.totalAmount.toMoneyFormat(),
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Button(
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    isShareBottomSheetIsVisible = true
                },
                modifier = Modifier.weight(1f)
            ) {
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
                onClick = {
                    onAction(SaleDetailsUiAction.Print(sale))
                },
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
        AnimatedVisibility(isShareBottomSheetIsVisible) {
            ModalBottomSheet(
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                ),
                onDismissRequest = { isShareBottomSheetIsVisible = false },
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var emailFieldVisible by remember { mutableStateOf(false) }
                    var phoneFieldVisible by remember { mutableStateOf(true) }
                    var emailError by remember { mutableStateOf(false) }
                    var phoneError by remember { mutableStateOf(false) }
                    Text(
                        text = stringResource(R.string.share_receipt_via),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)

                    )
                    MultiOptionSwitch(
                        modifier = Modifier.heightIn(max = 48.dp),
                        options = listOf(
                            R.string.whatsapp,
                            R.string.email
                        ).map { stringResource(it) },
                        selectedIndex = if (emailFieldVisible) 1 else 0,
                        onOptionSelected = {
                            when (it) {
                                0 -> {
                                    emailFieldVisible = false
                                    phoneFieldVisible = true
                                }

                                1 -> {
                                    phoneFieldVisible = false
                                    emailFieldVisible = true
                                }
                            }
                        }
                    )
                    AnimatedContent(emailFieldVisible to phoneFieldVisible) { state ->
                        when {
                            state.first -> {
                                TextInputTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = uiState.storeSale.customerEmail,
                                    label = stringResource(R.string.email),
                                    onValueChange = {
                                        onAction(SaleDetailsUiAction.UpdateCustomerEmail(it))
                                    },
                                    imageVector = Icons.Default.Email,
                                    isError = emailError,
                                    errorMessage = R.string.not_a_valid_email
                                )
                            }

                            state.second -> {
                                PhoneNumberTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = uiState.storeSale.customerPhone,
                                    onValueChange = {
                                        onAction(SaleDetailsUiAction.UpdateCustomerPhoneNumber(it))
                                    },
                                    isError = phoneError,
                                    errorMessage = R.string.not_a_valid_phone_number
                                )
                            }
                        }

                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .heightIn(min = 48.dp),
                        onClick = {
                            if (phoneFieldVisible) {
                                if (uiState.storeSale.customerPhone.isValidPhoneNumber()) {
                                    phoneError = false
                                    onAction(SaleDetailsUiAction.ShareViaWhatsapp(sale))
                                } else {
                                    phoneError = true
                                }
                            } else {
                                if (android.util.Patterns.EMAIL_ADDRESS.matcher(uiState.storeSale.customerEmail)
                                        .matches()
                                ) {
                                    emailError = false
                                    onAction(SaleDetailsUiAction.ShareViaEmail(sale))
                                } else {
                                    emailError = true
                                }
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.share)
                        )
                    }

                }
            }
        }

    }

}