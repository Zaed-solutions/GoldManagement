package com.zaed.accountant.app.navigation

import com.zaed.common.data.model.customer.CustomerType
import kotlinx.serialization.Serializable

sealed interface Route{
    @Serializable
    data object SignUpRoute: Route
    @Serializable
    data object LoginRoute: Route
    @Serializable
    data object ChequesRoute: Route
    @Serializable
    data class AddCustomers(val customerId: String = "", val type: CustomerType = CustomerType.GOLD): Route
    @Serializable
    data class CustomerDetails(val customerId: String) : Route
    @Serializable
    data object WholeSaleCustomers : Route
    @Serializable
    data class ProductSaleDetailsRoute(val saleId: String) : Route
    @Serializable
    data class GoldSaleDetailsRoute(val saleId: String) : Route
    @Serializable
    data class PurchaseDetailsRoute(val purchaseId: String) : Route
    @Serializable
    data object PurchasesRoute : Route
    @Serializable
    data object SuppliersRoute : Route
    @Serializable
    data class SupplierDetailsRoute(val supplierId: String) : Route
}