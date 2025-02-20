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


