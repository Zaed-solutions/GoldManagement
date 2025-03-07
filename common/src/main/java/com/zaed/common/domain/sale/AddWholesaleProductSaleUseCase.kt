package com.zaed.common.domain.sale

import android.util.Log
import com.zaed.common.data.model.sale.request.AddWholesaleProductSaleRequest
import com.zaed.common.data.repository.SaleRepository

class AddWholesaleProductSaleUseCase(
    private val saleRepo: SaleRepository,
) {
    suspend operator fun invoke(request: AddWholesaleProductSaleRequest): Result<String> {
        Log.d("add_sale", "invoke: $request")
        return saleRepo.addWholesaleProductSale(request)
    }
}