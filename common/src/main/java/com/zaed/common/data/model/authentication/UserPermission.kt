package com.zaed.common.data.model.authentication

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.zaed.common.R

enum class UserPermission (@DrawableRes val iconRes: Int, @StringRes val titleRes: Int){
    SELL_PRODUCTS(R.drawable.ic_money_plus, R.string.sell_products),
    SELL_GOLD(R.drawable.ic_gold, R.string.sell_gold),
    SELL_SILVER(R.drawable.ic_shopping, R.string.sell_silver),
    SELL_INGOTS(R.drawable.ic_ingot, R.string.sell_ingots),
}