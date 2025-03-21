package com.zaed.common.data.source.remote

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.AggregateField
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DashboardRemoteSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : DashboardRemoteSource {
    private val storeSalesCollection = firestore.collection("store_sales")
    private val wholesalesCollection = firestore.collection("wholesale_sales")
    private val ingotTransactionsCollection = firestore.collection("ingot_transaction")
    override suspend fun getStoresProfits(): Result<Double> {
        try {
            val query = storeSalesCollection.whereEqualTo("deleted", false)
            val aggregateQuery = query.aggregate(AggregateField.sum("profit"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()

            val sum = result.get(AggregateField.sum("profit"))
            Log.d("DashboardRemoteSourceImpl", "getStoresProfits: $sum")

            return Result.success((sum as Double).toDouble())

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getWholesaleProfits(managerId: String): Result<Double> {
        try {
            val query = wholesalesCollection.where(
                Filter.and(
                    Filter.notEqualTo("distributorId", managerId),
                    Filter.equalTo("deleted", false)
                )
            )
            val aggregateQuery = query.aggregate(AggregateField.sum("profit"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()

            val sum = result.get(AggregateField.sum("profit"))
            return Result.success((sum as Double).toDouble())

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getManagerProfits(managerId: String): Result<Double> {
        try {
            val query = wholesalesCollection.where(
                Filter.and(
                    Filter.notEqualTo("distributorId", managerId),
                    Filter.equalTo("deleted", false)
                )
            )
            val aggregateQuery = query.aggregate(AggregateField.sum("profit"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()

            val sum = result.get(AggregateField.sum("profit"))
            return Result.success((sum as Double).toDouble())

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getStoreSales(): Result<Double> {
        try {
            Log.d("DashboardRemoteSourceImpl", "getStoreSales: start")

            val query = storeSalesCollection.whereEqualTo("deleted", false)
            val aggregateQuery = query.aggregate(AggregateField.sum("totalAmount"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()

            val sum = result.get(AggregateField.sum("totalAmount"))
            Log.d("DashboardRemoteSourceImpl", "getStoreSales: $sum")
            return Result.success((sum as Double).toDouble())

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            Log.d("DashboardRemoteSourceImpl", "getStoreSales: $e")
            return Result.failure(e)
        }
    }

    override suspend fun getManagerSales(managerId: String): Result<Double> {
        return Result.success(0.0)
    }

    override suspend fun getWholesaleSales(managerId: String): Result<Double> {
        return Result.success(0.0)
    }

    override suspend fun getStoreLoss(): Result<Double> {
        return Result.success(0.0)
    }

    override suspend fun getManagerLoss(): Result<Double> {
        return Result.success(0.0)
    }

    override suspend fun getWholesaleLoss(): Result<Double> {
        return Result.success(0.0)
    }
}