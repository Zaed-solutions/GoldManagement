package com.zaed.manager.ui.distributorssales.components

import com.zaed.common.data.model.category.Category
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.customer.WholeSaleCustomer
import java.util.Date

data class DistributorsSalesFilter(
    val startDate: Date? = null,
    val endDate: Date? = null,
    val locations: List<String> = emptyList(),
    val employees: List<User> = emptyList(),
    val customers: List<WholeSaleCustomer> = emptyList(),
    val categories: List<Category> = emptyList()
){
    val isFiltered: Boolean
        get() = startDate != null || endDate != null || locations.isNotEmpty() || employees.isNotEmpty() || customers.isNotEmpty() || categories.isNotEmpty()
}
