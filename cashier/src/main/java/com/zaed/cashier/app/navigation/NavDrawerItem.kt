package com.zaed.cashier.app.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.zaed.common.R

enum class NavDrawerItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int?,
    val route: Route
) {
    ADD_SALE(
        title = R.string.add_sale,
        icon = R.drawable.ic_gold,
        route = Route.AddSaleRoute()
    ),
    PRODUCT_SALES(
        title = R.string.sales,
        icon = R.drawable.ic_money_plus,
        route = Route.SalesRoute
    ),
    LOSSES(
        title = R.string.losses,
        icon = R.drawable.ic_money_minus,
        route = Route.LossRoute
    )
}