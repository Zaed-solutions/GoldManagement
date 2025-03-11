package com.zaed.common.domain.customer

import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.repository.WholeSalesCustomerRepository
import kotlinx.coroutines.flow.Flow

class GetWholeSalesCustomersUseCase(
    private val wholeSalesCustomerRepository: WholeSalesCustomerRepository
) {
    operator fun invoke(distributorId:String): Flow<Result<List<WholeSaleCustomer>>> {
        return wholeSalesCustomerRepository.getWholeSalesCustomers(distributorId)
    }

}