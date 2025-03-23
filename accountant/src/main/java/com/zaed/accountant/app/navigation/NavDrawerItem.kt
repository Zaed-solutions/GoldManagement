package com.zaed.accountant.app.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.zaed.common.R

enum class NavDrawerItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int?,
    val route: Route
) {
    Cheques(
        title = R.string.cheques,
        icon = R.drawable.ic_cheque,
        route = Route.ChequesRoute
    ),
    CUSTOMERS(
        title = R.string.customers,
        icon = R.drawable.ic_customers,
        route = Route.WholeSaleCustomers
    ),
    PURCHASES(
        title = R.string.purchases,
        icon = R.drawable.ic_shopping,
        route = Route.PurchasesRoute
    ),
    SUPPLIERS(
        title = R.string.suppliers,
        icon = R.drawable.ic_users,
        route = Route.SuppliersRoute
    ),
}