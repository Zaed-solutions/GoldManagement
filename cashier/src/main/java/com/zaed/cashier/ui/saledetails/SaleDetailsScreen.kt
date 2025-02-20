package com.zaed.cashier.ui.saledetails

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.cashier.ui.theme.CashierAppTheme
import com.zaed.common.data.model.sale.Discount
import com.zaed.common.data.model.sale.DiscountType
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.StoreSale
import com.zaed.common.ui.components.BackIcon
import com.zaed.common.ui.components.TextInputTextField
import com.zaed.common.ui.util.FileUtil
import com.zaed.common.ui.util.PhoneUtil
import com.zaed.common.ui.util.ReceiptUtil
import com.zaed.common.ui.util.isValidEmail
import com.zaed.common.ui.util.isValidPhoneNumber
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
    val context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.setSaleId(saleId)
    }
    SaleDetailsScreenContent(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                SaleDetailsUiAction.OnBack -> onBack()
                is SaleDetailsUiAction.Print -> {
                    ReceiptUtil.generateStoreSaleReceipt(
                        context = context,
                        logoMipmapId = R.mipmap.bg_receipt_header,
                        storeSale = action.storeSale
                    ).let {
//                        Toast.makeText(context, it.absolutePath, Toast.LENGTH_SHORT).show()
                        FileUtil.openFile(
                            context = context,
                            file = it,
                            type = "application/pdf"
                        ) {
                            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                is SaleDetailsUiAction.ShareViaWhatsapp -> {
                    ReceiptUtil.generateStoreSaleReceipt(
                        context = context,
                        logoMipmapId = R.mipmap.bg_receipt_header,
                        storeSale = action.storeSale
                    ).let {
//                        Toast.makeText(context, it.absolutePath, Toast.LENGTH_SHORT).show()
                        PhoneUtil.sendReceiptViaWhatsapp(
                            context = context,
                            phoneNumber = action.storeSale.customerPhoneNumber,
                            file = it,
                        ) {
                            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                is SaleDetailsUiAction.ShareViaEmail -> {
                    ReceiptUtil.generateStoreSaleReceipt(
                        context = context,
                        logoMipmapId = R.mipmap.bg_receipt_header,
                        storeSale = action.storeSale
                    ).let {
                        PhoneUtil.shareReceiptViaEmail(
                            email = action.storeSale.customerEmail,
                            context = context,
                            file = it,
                        ) {}
                    }
                }
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
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.sale_details)
                    )
                },
                navigationIcon = {
                    BackIcon {
                        onAction(SaleDetailsUiAction.OnBack)
                    }
                }
            )
        }
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SaleDetailsPreview(uiState = uiState, onAction = onAction)
        }
    }
}

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
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Text(
                        text = if (sale.id.isEmpty()) "" else sale.id.take(7),
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
                        text = stringResource(R.string.phone_number_colon),
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
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
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
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
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
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
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
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    var emailFieldVisible by remember { mutableStateOf(false) }
                    var phoneFieldVisible by remember { mutableStateOf(false) }
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
                    AnimatedVisibility(
                        visible = emailFieldVisible,
                        enter = androidx.compose.animation.slideInVertically(),
                        exit = androidx.compose.animation.slideOutVertically()
                    ) {
                        TextInputTextField(
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
                    AnimatedVisibility(
                        visible = phoneFieldVisible,
                        enter = androidx.compose.animation.slideInVertically(),
                        exit = androidx.compose.animation.slideOutVertically()
                    ) {
                        TextInputTextField(
                            value = uiState.storeSale.customerPhoneNumber,
                            label = stringResource(R.string.phone_number),
                            onValueChange = {
                                onAction(SaleDetailsUiAction.UpdateCustomerPhoneNumber(it))
                            },
                            imageVector = Icons.Default.Phone,
                            isError = phoneError,
                            errorMessage = R.string.not_a_valid_phone_number
                        )
                    }
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            onClick = {
                                if (uiState.storeSale.customerPhoneNumber.isEmpty()) {
                                    emailFieldVisible = false
                                    phoneFieldVisible = true
                                } else {
                                    if(uiState.storeSale.customerPhoneNumber.isValidPhoneNumber()){
                                        phoneError = false
                                        onAction(SaleDetailsUiAction.ShareViaWhatsapp(sale))
                                    }else{
                                        phoneError = true
                                    }
                                }
                            }
                        ) {
                            Row(
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Whatsapp,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = stringResource(R.string.whatsapp))
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        FilledTonalButton(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            onClick = {
                                if (uiState.storeSale.customerEmail.isEmpty()) {
                                    phoneFieldVisible = false
                                    emailFieldVisible = true
                                } else {
                                    if(uiState.storeSale.customerEmail.isValidEmail()){
                                        emailError = false
                                        onAction(SaleDetailsUiAction.ShareViaEmail(sale))
                                    }else{
                                        emailError = true
                                    }
                                }
                            }
                        ) {
                            Row(
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = stringResource(R.string.email))
                            }
                        }
                    }
                }
            }
        }

    }

}


@Preview(locale = "ar")
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
                            gramPrice = 100.0,
                            grams = 5.0,
                        ),
                        Product(
                            id = "123456789",
                            name = "Product 2",
                            gramPrice = 200.0,
                            grams = 10.0,
                        )
                    ),
                    discount = Discount(
                        type = DiscountType.AMOUNT,
                        value = 10.0,
                    ),
                ),

                ),
            onAction = {}
        )
    }
}
