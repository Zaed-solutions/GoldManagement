package com.zaed.common.domain

import com.zaed.common.data.model.Payment
import com.zaed.common.data.model.request.FetchCustomerPaymentsRequest
import com.zaed.common.data.repository.WholeSalesCustomerRepository
import kotlinx.coroutines.flow.Flow

class FetchCustomerPaymentsUseCase(
        private val customerRepository: WholeSalesCustomerRepository
) {
    operator fun invoke(request: FetchCustomerPaymentsRequest): Flow<Result<List<Payment>>>{
        return customerRepository.fetchCustomerPayments(request)
    }
}