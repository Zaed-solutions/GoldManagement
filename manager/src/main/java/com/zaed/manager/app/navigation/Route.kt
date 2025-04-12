package com.zaed.manager.app.navigation

import com.zaed.common.data.model.customer.CustomerType
import com.zaed.common.ui.addpurchase.ProductType
import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object UserManagementRoute : Route
    @Serializable
    data object LoginRoute : Route
    @Serializable
    data object SignUpRoute : Route
    @Serializable
    data object WholeSaleCustomers : Route
    @Serializable
    data object StoresOverviewRoute : Route
    @Serializable
    data class StoresSalesRoute(val startDate: String?=null, val endDate: String?=null) : Route
    @Serializable
    data class StoreDetailsRoute(val storeId: String) : Route
    @Serializable
    data class PurchaseDetailsRoute(val purchaseId: String) : Route
    @Serializable
    data object DistributorsRoute : Route
    @Serializable
    data class WholesaleOverviewRoute(val type: ProductType) : Route
    @Serializable
    data class DistributorsSalesRoute(val startDate: String?=null, val endDate: String?=null, val isOutStanding : Boolean = false): Route
    @Serializable
    data class AddCustomers(val customerId: String = "", val type: CustomerType = CustomerType.GOLD) : Route
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
    data class SupplierDetailsRoute(val supplierId: String) : Route
    @Serializable
    data class AddPurchaseRoute(val purchaseId: String = "") : Route
    @Serializable
    data object TransactionsRoute: Route
    @Serializable
    data object PendingBillsRoute: Route
    @Serializable
    data object DashboardRoute: Route
    @Serializable
    data object CategoriesRoute: Route
}