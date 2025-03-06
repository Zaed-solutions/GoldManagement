package com.zaed.manager.app.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object UserManagementRoute : Route
    @Serializable
    data object LoginRoute : Route
    @Serializable
    data object HomeRoute : Route
    @Serializable
    data object SignUpRoute : Route
    @Serializable
    data object StoresRoute : Route
    @Serializable
    data class StoreDetailsRoute(val storeId: String) : Route
    @Serializable
    data object DistributorsRoute : Route
}