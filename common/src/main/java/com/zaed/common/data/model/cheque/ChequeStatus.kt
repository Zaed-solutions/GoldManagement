package com.zaed.common.data.model.cheque

import androidx.annotation.StringRes
import com.zaed.common.R

enum class ChequeStatus(@StringRes val titleRes: Int){
    RECEIVED(R.string.received),
    CASHED(R.string.cashed),
    TRANSFERRED(R.string.transfered),
}