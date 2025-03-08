package com.zaed.cashier.ui.saledetails.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zaed.cashier.ui.saledetails.SaleDetailsUiAction
import com.zaed.cashier.ui.saledetails.SaleDetailsUiState
import com.zaed.common.R
import com.zaed.common.ui.components.BackIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleDetailsScreenContent(
    uiState: SaleDetailsUiState,
    onAction: (SaleDetailsUiAction) -> Unit
) {
    BackHandler {
        onAction(SaleDetailsUiAction.OnBack)
    }
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