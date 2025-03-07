package com.zaed.distributor.app.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.zaed.common.R
import com.zaed.common.data.model.authentication.UserPermission

enum class NavDrawerItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int?,
    val route: Route
) {
    PRODUCT_SALES(
        title = R.string.sales,
        icon = R.drawable.ic_money_plus,
        route = Route.SalesRoute
    ),
    ADD_SALE(
        title = R.string.add_sale,
        icon = R.drawable.ic_gold,
        route = Route.AddProductSaleRoute()
    ),
    CUSTOMERS(
        title = R.string.customers,
        icon = R.drawable.ic_customers,
        route = Route.WholeSaleCustomers
    ),
    LOSSES(
        title = R.string.losses,
        icon = R.drawable.ic_money_minus,
        route = Route.LossesRoute
    ),
    GOLD_SALES(
        title = R.string.gold_sales,
        icon = R.drawable.ic_coins,
        route = Route.AddGoldSaleRoute()
    ),
    INGOTS_SALES(
        title = R.string.ingots_sales,
        icon = R.drawable.ic_ingot,
        route = Route.IngotTransactionsRoute
    ),
}

fun List<UserPermission>.mapToNavDrawerItems(): List<NavDrawerItem> {
    val routes = mutableListOf<NavDrawerItem>()
    forEach {
        when (it) {
            UserPermission.SELL_PRODUCTS -> {
                routes.add(NavDrawerItem.ADD_SALE)
                routes.add(NavDrawerItem.PRODUCT_SALES)
            }

            UserPermission.SELL_GOLD -> {
                routes.add(NavDrawerItem.GOLD_SALES)
            }
            UserPermission.SELL_INGOTS -> {
                routes.add(NavDrawerItem.INGOTS_SALES)
            }
        }
    }
    routes.addAll(
        listOf(
            NavDrawerItem.CUSTOMERS,
            NavDrawerItem.LOSSES
        )
    )
    return routes
}