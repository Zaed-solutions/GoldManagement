package com.zaed.common.domain.customer

import com.zaed.common.data.model.customer.request.FetchWholesaleCustomerSalesRequest
import com.zaed.common.data.model.sale.WholesaleSale
import com.zaed.common.data.repository.SaleRepository
import kotlinx.coroutines.flow.Flow

class FetchWholesaleCustomerSalesUseCase(
    private val saleRepository: SaleRepository
) {
    operator fun invoke(request: FetchWholesaleCustomerSalesRequest): Flow<Result<List<WholesaleSale>>> {
        return saleRepository.fetchWholesaleCustomerSales(request)
    }
}