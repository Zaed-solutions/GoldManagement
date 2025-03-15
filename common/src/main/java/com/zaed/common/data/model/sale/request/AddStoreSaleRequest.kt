package com.zaed.common.data.model.sale.request

import com.zaed.common.data.model.sale.StoreTransaction

data class AddStoreSaleRequest(
    val sale: StoreTransaction
)
