package com.zaed.common.data.model

import androidx.annotation.StringRes
import com.zaed.common.R

enum class DiscountType(@StringRes val titleRes: Int) {
    NONE(R.string.none),
    PERCENTAGE(R.string.percentage_discount),
    AMOUNT(R.string.amount_discount)
}