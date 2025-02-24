package com.zaed.common.data.model.payment

import androidx.annotation.StringRes

enum class PaymentStatus(@StringRes val label: Int) {
    ALL(com.zaed.common.R.string.all),
    PAID(com.zaed.common.R.string.paid),
    UNPAID(com.zaed.common.R.string.unpaid),
    SPECIFYING_KARAT(com.zaed.common.R.string.specifying_karat)
}