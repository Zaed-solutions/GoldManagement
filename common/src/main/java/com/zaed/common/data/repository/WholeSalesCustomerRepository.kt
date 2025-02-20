package com.zaed.common.data.repository

import com.zaed.common.data.model.WholeSaleCustomer
import com.zaed.common.data.model.request.AddWholeSaleCustomerRequest
import com.zaed.common.domain.UpdateCustomerDebtRequest
import kotlinx.coroutines.flow.Flow

interface WholeSalesCustomerRepository {
    fun getWholeSalesCustomers(): Flow<Result<List<WholeSaleCustomer>>>
    suspend fun addWholeSaleCustomer(request: AddWholeSaleCustomerRequest): Result<Unit>
    suspend fun getWholeSaleCustomer(customerId: String): Result<WholeSaleCustomer>
    suspend fun updateCustomerDebt(updateCustomerDebtRequest: UpdateCustomerDebtRequest): Result<Unit>
}
