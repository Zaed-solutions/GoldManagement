package com.zaed.common.domain.customer

import com.zaed.common.data.model.customer.request.EditWholeSalesCustomerRequest
import com.zaed.common.data.repository.WholeSalesCustomerRepository

class EditWholeSalesCustomerUseCase(
    private val repository: WholeSalesCustomerRepository
) {
    suspend operator fun invoke(request: EditWholeSalesCustomerRequest) =
        repository.editWholeSalesCustomer(request)

}


