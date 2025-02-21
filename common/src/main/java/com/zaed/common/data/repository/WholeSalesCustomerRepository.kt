package com.zaed.common.data.repository

import com.zaed.common.data.model.customer.AddWholeSaleCustomerRequest
import com.zaed.common.data.model.customer.FetchWholesaleCustomersByNameRequest
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.customer.request.EditWholeSalesCustomerRequest
import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.model.payment.request.DeletePaymentRequest
import com.zaed.common.domain.payment.UpdateCustomerDebtRequest
import kotlinx.coroutines.flow.Flow

interface WholeSalesCustomerRepository {
    fun getWholeSalesCustomers(): Flow<Result<List<WholeSaleCustomer>>>
    suspend fun addWholeSaleCustomer(request: AddWholeSaleCustomerRequest): Result<Unit>
    suspend fun getWholeSaleCustomer(customerId: String): Result<WholeSaleCustomer>
    suspend fun updateCustomerDebt(updateCustomerDebtRequest: UpdateCustomerDebtRequest): Result<Unit>
    suspend fun deletePayment(request: DeletePaymentRequest): Result<Unit>
    suspend fun addNewPayment(request: AddNewPaymentRequest): Result<Unit>
    suspend fun fetchWholesaleCustomersByName(request: FetchWholesaleCustomersByNameRequest): Result<List<WholeSaleCustomer>>
    suspend fun deleteCustomer(customerId: String): Result<Unit>
    suspend fun editWholeSalesCustomer(request: EditWholeSalesCustomerRequest): Result<Unit>
}
