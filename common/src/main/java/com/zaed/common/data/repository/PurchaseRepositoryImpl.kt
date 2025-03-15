package com.zaed.common.data.repository

import com.zaed.common.data.model.purchase.Purchase
import com.zaed.common.data.model.sale.request.AddPurchaseRequest
import com.zaed.common.data.model.sale.request.UpdatePurchaseRequest
import com.zaed.common.data.source.remote.PurchaseRemoteDataSource

class PurchaseRepositoryImpl(
    private val purchaseRemoteDataSource: PurchaseRemoteDataSource
) : PurchaseRepository {
    override suspend fun fetchPurchaseById(id: String): Result<Purchase> {
        return  purchaseRemoteDataSource.fetchPurchaseById(id)
    }

    override suspend fun addPurchase(request: AddPurchaseRequest): Result<String> {
        return purchaseRemoteDataSource.addPurchase(request)
    }

    override suspend fun updatePurchase(request: UpdatePurchaseRequest): Result<String> {
        return purchaseRemoteDataSource.updatePurchase(request)
    }
}