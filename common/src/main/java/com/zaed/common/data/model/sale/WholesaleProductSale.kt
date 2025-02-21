package com.zaed.common.data.model.sale

import com.zaed.common.data.model.authentication.ChangeLog
import kotlinx.serialization.Transient
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
) : WholesaleSale(){
    @Transient
    val totalPrice
        get() = products.sumOf { it.grams*it.gramPrice }
}