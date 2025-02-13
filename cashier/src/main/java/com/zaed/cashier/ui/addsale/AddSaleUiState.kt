package com.zaed.cashier.ui.addsale

import com.zaed.common.data.model.StoreSale

data class AddSaleUiState(
    val sale: StoreSale = StoreSale(),
    val isFinished: Boolean = false
)
