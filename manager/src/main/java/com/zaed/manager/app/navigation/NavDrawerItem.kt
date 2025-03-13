package com.zaed.manager.app.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.zaed.common.R

enum class NavDrawerItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int?,
    val route: Route
) {
    USER_MANAGEMENT(
        title = R.string.user_management,
        icon = R.drawable.ic_users,
        route = Route.UserManagementRoute
    ),
    MANUFACTURER_ORDERS(
        title = R.string.manufacturer_orders,
        icon = R.drawable.ic_order,
        route = Route.ManufacturerOrdersRoute
    ),
    STORES(
        title = com.zaed.common.R.string.stores,
        icon = R.drawable.ic_store,
        route = Route.StoresRoute
    ),
    STORES_SALES(
        title = R.string.stores_sales,
        icon = R.drawable.ic_money_plus,
        route = Route.StoresSalesRoute
    ),
    CUSTOMERS(
        title = R.string.customers,
        icon = R.drawable.ic_customers,
        route = Route.WholeSaleCustomers
    ),
    DISTRIBUTORS(
        title = R.string.distributors,
        icon = R.drawable.ic_users,
        route = Route.DistributorsRoute
    ),
    DISTRIBUTORS_SALES(
        title = R.string.distributors_sales,
        icon = R.drawable.ic_money_plus,
        route = Route.DistributorsSalesRoute
    ),
    LOSSES(
        title = R.string.losses,
        icon = R.drawable.ic_money_minus,
        route = Route.LossesRoute
    ),
    INGOTS_SALES(
        title = R.string.ingots_transactions,
        icon = R.drawable.ic_ingot,
        route = Route.IngotTransactionsRoute
    ),
    GOLD_SALES(
        title = R.string.gold_sales,
        icon = R.drawable.ic_coins,
        route = Route.AddGoldSaleRoute()
    ),
    Cheques(
        title = R.string.cheques,
        icon = R.drawable.ic_cheque,
        route = Route.ChequesRoute
    ),

}