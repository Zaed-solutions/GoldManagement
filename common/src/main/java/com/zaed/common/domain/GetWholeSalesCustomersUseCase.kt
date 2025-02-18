package com.zaed.common.domain

import com.zaed.common.data.model.WholeSaleCustomer
import com.zaed.common.data.repository.WholeSalesCustomerRepository
import kotlinx.coroutines.flow.Flow

class GetWholeSalesCustomersUseCase(
    private val wholeSalesCustomerRepository: WholeSalesCustomerRepository
) {
    operator fun invoke(): Flow<Result<List<WholeSaleCustomer>>> {
        return wholeSalesCustomerRepository.getWholeSalesCustomers()
    }

}
