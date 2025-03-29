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
    ADD_SALE(
        title = R.string.add_product_sale,
        icon = R.drawable.ic_add,
        route = Route.AddProductSaleRoute()
    ),
    PRODUCT_SALES(
        title = R.string.sales,
        icon = R.drawable.ic_money_plus,
        route = Route.SalesRoute()
    ),
    OUTSTANDING_SALES(
        title = com.zaed.common.R.string.outstanding_bills,
        icon = R.drawable.ic_gold,
        route = Route.SalesRoute(true)
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
        title = R.string.add_gold_sale,
        icon = R.drawable.ic_add,
        route = Route.AddGoldSaleRoute()
    ),
    ADD_SILVER_SALE(
        title = R.string.add_silver_sale,
        icon = R.drawable.ic_add,
        route = Route.AddSilverSaleRoute()
    ),
    INGOTS_SALES(
        title = R.string.ingots_transactions,
        icon = R.drawable.ic_ingot,
        route = Route.IngotTransactionsRoute
    ),
}

fun List<UserPermission>.mapToNavDrawerItems(): List<NavDrawerItem> {
    val routes = mutableListOf<NavDrawerItem>()
    this.sortedBy { it.ordinal }.forEach {
        when (it) {
            UserPermission.SELL_PRODUCTS -> {
                routes.add(NavDrawerItem.ADD_SALE)
                routes.add(NavDrawerItem.PRODUCT_SALES)
            }

            UserPermission.SELL_GOLD -> {
                routes.add(NavDrawerItem.GOLD_SALES)
                routes.add(NavDrawerItem.OUTSTANDING_SALES)
            }

            UserPermission.SELL_SILVER -> {
                routes.add(NavDrawerItem.ADD_SILVER_SALE)
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