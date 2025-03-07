package com.zaed.common.data.model.authentication

import androidx.annotation.DrawableRes
import com.zaed.common.R

enum class UserPermission (@DrawableRes val iconRes: Int){
    SELL_PRODUCTS(R.drawable.ic_money_plus),
    SELL_GOLD(R.drawable.ic_gold),
    SELL_INGOTS(R.drawable.ic_ingot),
}