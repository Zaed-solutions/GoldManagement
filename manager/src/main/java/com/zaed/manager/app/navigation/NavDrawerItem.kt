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
    STORES(
        title = com.zaed.manager.R.string.stores,
        icon = R.drawable.ic_store,
        route = Route.StoresRoute
    ),
    DISTRIBUTORS(
        title = R.string.distributors,
        icon = R.drawable.ic_users,
        route = Route.DistributorsRoute
    )
}