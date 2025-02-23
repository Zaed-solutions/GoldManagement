package com.zaed.common.data.model.payment

import com.zaed.common.data.model.DropdownMenuItem

enum class PaymentType :DropdownMenuItem {
    CASH,
    BANK_TRANSFER,
    CHEQUE,
    FUTURES,
    GOLD
}