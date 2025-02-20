package com.zaed.common.data.model

import java.util.Date

data class WholesaleProductSale(
    override val id: String = "",
    override val customerId: String = "",
    override val customerName: String = "",
    override val customerPhone: String = "",
    override val distributorId: String = "",
    override val distributorName: String = "",
    override val createdAt: Date = Date(),
    override val logs: List<ChangeLog> = emptyList(),
    override val deleted: Boolean = false,
    val products: List<Product> = emptyList(),
    val paymentsIds: List<String> = emptyList(),
    override val paid: Boolean = false,
    override val receiptNumber: String = "",
) : WholesaleSale()