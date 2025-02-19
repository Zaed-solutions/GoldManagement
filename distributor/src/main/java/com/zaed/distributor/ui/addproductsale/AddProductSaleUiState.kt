package com.zaed.distributor.ui.addproductsale

import com.zaed.common.data.model.Category
import com.zaed.common.data.model.User
import com.zaed.common.data.model.WholesaleProductSale

data class AddProductSaleUiState(
    val isFinished: Boolean = false,
    val sale: WholesaleProductSale = WholesaleProductSale(),
    val currentUser: User = User(),
    val categories: List<Category> = emptyList()
)
