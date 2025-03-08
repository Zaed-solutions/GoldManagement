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
        title = R.string.add_sale,
        icon = R.drawable.ic_users,
        route = Route.UserManagementRoute
    ),
    STORES(
        title = R.string.sales,
        icon = R.drawable.ic_store,
        route = Route.StoresRoute
    ),
    DISTRIBUTORS(
        title = R.string.losses,
        icon = R.drawable.ic_users,
        route = Route.DistributorsRoute
    )
}