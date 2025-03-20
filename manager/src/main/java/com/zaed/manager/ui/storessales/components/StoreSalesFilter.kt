package com.zaed.manager.ui.storessales.components

import com.zaed.common.data.model.category.Category
import com.zaed.common.data.model.authentication.User
import java.util.Date

data class StoreSalesFilter(
    val startDate: Date? = null,
    val endDate: Date? = null,
    val locations: List<String> = emptyList(),
    val employees: List<User> = emptyList(),
    val customers: List<String> = emptyList(),
    val categories: List<Category> = emptyList()
){
    val isFiltered: Boolean
        get() = startDate != null || endDate != null || locations.isNotEmpty() || employees.isNotEmpty() || customers.isNotEmpty() || categories.isNotEmpty()
}
