package com.zaed.common.ui.addcustomers

import androidx.annotation.StringRes

enum class AddCustomersScreenError(@StringRes val message: Int) {
    NO_INTERNET(com.zaed.common.R.string.no_internet_connection),
    UNKNOWN(com.zaed.common.R.string.unknown_error)
}