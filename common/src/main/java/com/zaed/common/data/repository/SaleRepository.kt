package com.zaed.common.data.repository

import com.zaed.common.data.model.StoreSale
import com.zaed.common.data.model.WholesaleSale
import com.zaed.common.data.model.request.AddStoreSaleRequest
import com.zaed.common.data.model.request.DeleteStoreSaleRequest
import com.zaed.common.data.model.request.DeleteWholesaleGoldSaleRequest
import com.zaed.common.data.model.request.DeleteWholesaleProductSaleRequest
import com.zaed.common.data.model.request.FetchDistributorSalesRequest
import com.zaed.common.data.model.request.FetchStoreSalesRequest
import com.zaed.common.data.model.request.UpdateStoreSaleRequest
import kotlinx.coroutines.flow.Flow

interface SaleRepository {
    fun fetchStoreSales(request: FetchStoreSalesRequest): Flow<Result<List<StoreSale>>>
    suspend fun addStoreSale(request: AddStoreSaleRequest): Result<String>
    suspend fun deleteStoreSale(request: DeleteStoreSaleRequest): Result<Unit>
    suspend fun updateStoreSale(request: UpdateStoreSaleRequest): Result<Unit>
    suspend fun getStoreSale(saleId: String): Result<StoreSale>
    fun fetchWholesaleDistributorSales(request: FetchDistributorSalesRequest): Flow<Result<List<WholesaleSale>>>
    suspend fun deleteWholesaleProductSale(request: DeleteWholesaleProductSaleRequest): Result<Unit>
    suspend fun deleteWholesaleGoldSale(request: DeleteWholesaleGoldSaleRequest): Result<Unit>
}