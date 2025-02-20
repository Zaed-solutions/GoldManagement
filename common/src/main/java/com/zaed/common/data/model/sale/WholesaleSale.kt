package com.zaed.common.data.model.sale

import com.zaed.common.data.model.authentication.ChangeLog
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
    abstract val receiptNumber: String
    abstract val deleted: Boolean
    abstract val paid: Boolean
}


