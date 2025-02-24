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
        icon = null,
        route = Route.SalesRoute
    ),
    CUSTOMERS(
        title = R.string.customers,
        icon = null,
        route = Route.WholeSaleCustomers
    ),
    LOSSES(
        title = R.string.losses,
        icon = null,
        route = Route.LossesRoute
    ),
    GOLD_SALES(
        title = R.string.gold_sales,
        icon = null,
        route = Route.AddGoldSaleRoute()
    ),
    INGOTS_SALES(
        title = R.string.ingots_sales,
        icon = null,
        route = Route.IngotTransactionsRoute
    ),
}

fun List<UserPermission>.mapToNavDrawerItems(): List<NavDrawerItem> {
    val routes = mutableListOf<NavDrawerItem>()
    forEach {
        when (it) {
            UserPermission.SELL_PRODUCTS -> {
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