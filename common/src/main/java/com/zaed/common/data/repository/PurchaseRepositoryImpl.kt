package com.zaed.common.data.repository

import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.data.model.sale.request.AddPurchaseRequest
import com.zaed.common.data.model.sale.request.DeleteWholesaleRequest
import com.zaed.common.data.model.sale.request.UpdatePurchaseRequest
import com.zaed.common.data.source.remote.PurchaseRemoteDataSource
import kotlinx.coroutines.flow.Flow

class PurchaseRepositoryImpl(
    private val purchaseRemoteDataSource: PurchaseRemoteDataSource
) : PurchaseRepository {
    override suspend fun fetchPurchaseById(id: String): Result<WholesaleTransaction> {
        return  purchaseRemoteDataSource.fetchPurchaseById(id)
    }

    override suspend fun addPurchase(request: AddPurchaseRequest): Result<String> {
        return purchaseRemoteDataSource.addPurchase(request)
    }

    override suspend fun updatePurchase(request: UpdatePurchaseRequest): Result<String> {
        return purchaseRemoteDataSource.updatePurchase(request)
    }

    override suspend fun deletePurchase(request: DeleteWholesaleRequest): Result<Unit> {
        return purchaseRemoteDataSource.deletePurchase(request)
    }

    override fun fetchPurchasesUseCase(): Flow<Result<List<WholesaleTransaction>>> {
        return purchaseRemoteDataSource.fetchPurchases()
    }
}