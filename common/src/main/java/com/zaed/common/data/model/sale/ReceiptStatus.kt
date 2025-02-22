package com.zaed.common.data.model.sale

import androidx.annotation.StringRes
import com.zaed.common.R

enum class ReceiptStatus(@StringRes val titleRes: Int) {
    PENDING(R.string.pending),
    RECEIVED(R.string.received),
    NOT_REQUESTED(R.string.not_requested)
}