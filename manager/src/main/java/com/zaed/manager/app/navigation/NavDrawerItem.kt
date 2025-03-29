package com.zaed.manager.app.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.zaed.common.R

enum class NavDrawerItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int?,
    val route: Route? = null,
    val subItems: List<SubNavDrawerItem> = emptyList()
) {
    DASHBOARD(
        title = R.string.dashboard,
        icon = R.drawable.ic_order,
        route = Route.DashboardRoute
    ),
    STORES_MAIN(
        title = R.string.stores,
        icon = R.drawable.ic_store,
        subItems = listOf(
            SubNavDrawerItem(
                title = R.string.stores,
                route = Route.StoresRoute
            ),
            SubNavDrawerItem(
                title = R.string.stores_sales,
                route = Route.StoresSalesRoute()
            )
        )
    ),
    WHOLESALE_MAIN(
        title = R.string.wholesale,
        icon = R.drawable.ic_users,
        subItems = listOf(
            SubNavDrawerItem(
                title = R.string.customers,
                route = Route.WholeSaleCustomers
            ),
            SubNavDrawerItem(
                title = R.string.distributors,
                route = Route.DistributorsRoute
            ),
            SubNavDrawerItem(
                title = R.string.distributors_sales,
                route = Route.DistributorsSalesRoute()
            ),
            SubNavDrawerItem(
                title = R.string.outstanding_bills,
                route = Route.DistributorsSalesRoute(isOutStanding = true)
            )
        )
    ),
    PURCHASES_MAIN(
        title = R.string.purchases,
        icon = R.drawable.ic_shopping,
        subItems = listOf(
            SubNavDrawerItem(
                title = R.string.suppliers,
                route = Route.SuppliersRoute
            ),
            SubNavDrawerItem(
                title = R.string.add_purchase,
                route = Route.AddPurchaseRoute()
            )
        )
    ),
    MANAGER_MAIN(
        title = R.string.manager,
        icon = R.drawable.ic_person,
        subItems = listOf(
            SubNavDrawerItem(
                title = R.string.user_management,
                route = Route.UserManagementRoute
            ),
            SubNavDrawerItem(
                title = R.string.manufacturer_orders,
                route = Route.ManufacturerOrdersRoute

            ),
            SubNavDrawerItem(
                title = R.string.categories,
                route = Route.CategoriesRoute
            ),
            SubNavDrawerItem(
                title = R.string.transactions,
                route = Route.TransactionsRoute
            ),
            SubNavDrawerItem(
                title = R.string.ingots_transactions,
                route = Route.IngotTransactionsRoute
            ),
            SubNavDrawerItem(
                title = R.string.gold_sales,
                route = Route.AddGoldSaleRoute()
            ),
            SubNavDrawerItem(
                title = R.string.cheques,
                route = Route.ChequesRoute
            ),
            SubNavDrawerItem(
                title = R.string.losses,
                route = Route.LossesRoute
            ),

            )
    ),
}

data class SubNavDrawerItem(
    @StringRes val title: Int,
    val route: Route
)