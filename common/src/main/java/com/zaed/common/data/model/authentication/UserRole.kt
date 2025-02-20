package com.zaed.common.data.model.authentication

import androidx.annotation.StringRes
import com.zaed.common.R

enum class UserRole(@StringRes val value: Int){
    NONE(0),
    CASHIER(R.string.cashier),
    MANAGER(R.string.manager),
    ACCOUNTANT(R.string.accountant),
    DISTRIBUTOR(R.string.distributor)
}