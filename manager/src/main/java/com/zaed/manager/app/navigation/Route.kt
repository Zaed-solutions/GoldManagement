package com.zaed.manager.app.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object UserManagementRoute : Route
    @Serializable
    data object LoginRoute : Route
    @Serializable
    data object SignUpRoute : Route
    @Serializable
    data object StoresRoute : Route
    @Serializable
    data object WholeSaleCustomers : Route
    @Serializable
    data object StoresSalesRoute : Route
    @Serializable
    data class StoreDetailsRoute(val storeId: String) : Route
    @Serializable
    data class PurchaseDetailsRoute(val purchaseId: String) : Route
    @Serializable
    data object DistributorsRoute : Route
    @Serializable
    data object DistributorsSalesRoute: Route
    @Serializable
    data class AddCustomers(val customerId: String = "") : Route
    @Serializable
    data object IngotTransactionsRoute : Route
    @Serializable
    data class CustomerDetails(val customerId: String) : Route
    @Serializable
    data class DistributorDetailsRoute(val distributorId: String) : Route
    @Serializable
    data object LossesRoute: Route
    @Serializable
    data object ChequesRoute: Route
    @Serializable
    data object ManufacturerOrdersRoute: Route
    @Serializable
    data class ProductSaleDetailsRoute(val saleId: String) : Route
    @Serializable
    data class GoldSaleDetailsRoute(val saleId: String) : Route
    @Serializable
    data class StoreSaleDetailsRoute(val saleId: String): Route
    @Serializable
    data class AddGoldSaleRoute(val saleId: String = "") : Route
    @Serializable
    data object SuppliersRoute : Route
    @Serializable
    data class AddPurchaseRoute(val purchaseId: String = "") : Route
}