package com.zaed.common.ui.saledetails.cashiersaledetails

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.zaed.common.R
import com.zaed.common.ui.saledetails.cashiersaledetails.component.SaleDetailsScreenContent
import com.zaed.common.ui.saledetails.productsaledetails.SaleDetailsUiAction
import com.zaed.common.ui.util.FileUtil
import com.zaed.common.ui.util.PhoneUtil
import com.zaed.common.ui.util.ReceiptUtil
import org.koin.androidx.compose.koinViewModel

@Composable
fun SaleDetailsScreen(
    viewModel: SaleDetailsViewModel = koinViewModel(),
    saleId: String,
    onBack: () -> Unit,
    onNavigateToEditSale: (String) -> Unit,
    isAdmin :Boolean = false
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.setSaleId(saleId)
    }
    LaunchedEffect(uiState.isSaleDeleted) {
        if (uiState.isSaleDeleted) {
            onBack()
        }
    }
    SaleDetailsScreenContent(
        state = uiState,
        isAdmin = isAdmin,
        onAction = { action ->
            when (action) {
                SaleDetailsUiAction.OnBackClicked -> onBack()
                SaleDetailsUiAction.OnEditClicked -> onNavigateToEditSale(saleId)
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
                            phoneNumber ="+212${action.storeSale.customerPhone}",
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

