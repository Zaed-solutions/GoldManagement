package com.zaed.common.data.model

import java.util.Date

sealed class WholesaleSale {
    abstract val id: String
    abstract val customerId: String
    abstract val customerName: String
    abstract val customerPhone: String
    abstract val distributorId: String
    abstract val distributorName: String
    abstract val createdAt: Date
    abstract val logs: List<ChangeLog>
    abstract val deleted: Boolean
    abstract val paid: Boolean
}


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
    val paymentId: String = "",
    override val paid: Boolean = false,
) : WholesaleSale()

data class WholesaleGoldSale(
    override val id: String = "",
    override val customerId: String = "",
    override val customerName: String = "",
    override val customerPhone: String = "",
    override val distributorId: String = "",
    override val distributorName: String = "",
    override val createdAt: Date = Date(),
    override val logs: List<ChangeLog> = emptyList(),
    override val deleted: Boolean = false,
    override val paid: Boolean = false
): WholesaleSale()