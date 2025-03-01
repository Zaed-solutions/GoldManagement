package com.zaed.cashier.ui.saledetails

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.zaed.cashier.ui.saledetails.component.SaleDetailsScreenContent
import com.zaed.cashier.ui.theme.CashierAppTheme
import com.zaed.common.R
import com.zaed.common.data.model.sale.Discount
import com.zaed.common.data.model.sale.DiscountType
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.StoreSale
import com.zaed.common.ui.util.FileUtil
import com.zaed.common.ui.util.PhoneUtil
import com.zaed.common.ui.util.ReceiptUtil
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
                        logoMipmapId = R.mipmap.cachier_logo_header_round,
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
                        logoMipmapId = R.mipmap.cachier_logo_header_round,
                        storeSale = action.storeSale
                    ).let {
//                        Toast.makeText(context, it.absolutePath, Toast.LENGTH_SHORT).show()
                        PhoneUtil.sendReceiptViaWhatsapp(
                            context = context,
                            phoneNumber ="+212${action.storeSale.customerPhoneNumber}",
                            file = it,
                        ) {
                            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                is SaleDetailsUiAction.ShareViaEmail -> {
                    ReceiptUtil.generateStoreSaleReceipt(
                        context = context,
                        logoMipmapId = R.mipmap.cachier_logo_header_round,
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
                ),

                ),
            onAction = {}
        )
    }
}
