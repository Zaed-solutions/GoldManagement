package com.zaed.common.data.model.payment

import androidx.annotation.StringRes
import com.zaed.common.R
import com.zaed.common.data.model.DropdownMenuItem

enum class PaymentType(@StringRes val titleRes: Int,val iconRes:Int) :DropdownMenuItem {
    CASH(R.string.cash,R.drawable.ic_coins),
    BANK_TRANSFER(R.string.bank_transfer,R.drawable.bank_ic),
    CHEQUE(R.string.cheque,R.drawable.ic_cheque),
    FUTURES(R.string.futures,R.drawable.customer_credit),
    GOLD(R.string.gold,R.drawable.ic_ingot),
    LOSS(R.string.loss,R.drawable.ic_money_minus);
}

fun getPaymentTypeDropDownItems() = listOf(
    PaymentType.CASH,
    PaymentType.BANK_TRANSFER,
    PaymentType.CHEQUE
)
fun getProductSalePayments() = listOf(
    PaymentType.CASH,
    PaymentType.BANK_TRANSFER,
    PaymentType.CHEQUE,
)
fun getGoldSalePayments() = listOf(
    PaymentType.CASH,
    PaymentType.BANK_TRANSFER,
    PaymentType.CHEQUE,
    PaymentType.GOLD
)
