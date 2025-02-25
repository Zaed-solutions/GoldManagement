package com.zaed.common.data.model.payment

import androidx.annotation.StringRes
import com.zaed.common.R
import com.zaed.common.data.model.DropdownMenuItem

enum class PaymentType(@StringRes val titleRes: Int) :DropdownMenuItem {
    CASH(R.string.cash),
    BANK_TRANSFER(R.string.bank_transfer),
    CHEQUE(R.string.cheque),
    FUTURES(R.string.futures),
    GOLD(R.string.gold);
}

fun getPaymentTypeDropDownItems() = listOf(
    PaymentType.CASH,
    PaymentType.BANK_TRANSFER,
    PaymentType.CHEQUE
)
