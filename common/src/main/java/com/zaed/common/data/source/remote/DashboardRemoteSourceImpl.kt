package com.zaed.common.data.source.remote

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.AggregateField
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.ui.addpurchase.ProductType
import kotlinx.coroutines.tasks.await

class DashboardRemoteSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : DashboardRemoteSource {
    private val storeSalesCollection = firestore.collection("store_sales")
    private val wholesalesCollection = firestore.collection("wholesale_sales")
    private val ingotTransactionsCollection = firestore.collection("ingot_transactions")
    private val storeLossesCollection = firestore.collection("store-losses")
    private val distributorLossesCollection = firestore.collection("distributor-losses")
    private val managerLossesCollection = firestore.collection("manager-losses")
    override suspend fun getStoresProfits(): Result<Double> {
        try {
            val query = storeSalesCollection.whereEqualTo("deleted", false)
            val aggregateQuery = query.aggregate(AggregateField.sum("profit"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()

            val sum = result.get(AggregateField.sum("profit"))
            Log.d("DashboardRemoteSourceImpl", "getStoresProfits: $sum")
            return Result.success((sum as? Double)?.toDouble()?:0.0)

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getWholesaleProfits(managerId: String): Result<Double> {
        try {
            val distributorQuery = wholesalesCollection.where(
                Filter.and(
                    Filter.notEqualTo("distributorId", managerId),
                    Filter.equalTo("deleted", false),
                    Filter.equalTo("productType", ProductType.PRODUCT)
                )
            )
            val distributorAggregateQuery = distributorQuery.aggregate(AggregateField.sum("profit"))
            val distributorResult = distributorAggregateQuery.get(AggregateSource.SERVER).await()
            val ingotQuery = ingotTransactionsCollection.where(
                Filter.and(
                    Filter.notEqualTo("distributorId", managerId),
                    Filter.equalTo("deleted", false)
                )
            )
            val ingotAggregateQuery = ingotQuery.aggregate(AggregateField.sum("totalEarning"))
            val ingotResult = ingotAggregateQuery.get(AggregateSource.SERVER).await()

            val distributorResultSum = distributorResult.get(AggregateField.sum("profit"))
            val ingotResultSum = ingotResult.get(AggregateField.sum("totalEarning"))
            val sum = (distributorResultSum as? Double)?.toDouble()?:0.0
            Log.d("DashboardRemoteSourceImpl", "getWholesaleProfits dist: $sum")
            val sum2 =(ingotResultSum as? Double)?.toDouble()?:0.0
            Log.d("DashboardRemoteSourceImpl", "getWholesaleProfits ing: $sum2")
            return Result.success((sum.plus(sum2)))
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
                    Filter.equalTo("distributorId", managerId),
                    Filter.equalTo("deleted", false)
                )
            )
            val aggregateQuery = query.aggregate(AggregateField.sum("profit"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()

            val sum = result.get(AggregateField.sum("profit"))
            Log.d("DashboardRemoteSourceImpl", "getManagerProfits: $sum")
            return Result.success((sum as? Double)?.toDouble()?:0.0)

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getStoreSales(): Result<Double> {
        try {
            val query = storeSalesCollection.whereEqualTo("deleted", false)
            val aggregateQuery = query.aggregate(AggregateField.sum("totalAmount"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()

            val sum = result.get(AggregateField.sum("totalAmount"))
            Log.d("DashboardRemoteSourceImpl", "getStoreSales: $sum")
            return Result.success((sum as? Double)?.toDouble()?:0.0)

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            Log.d("DashboardRemoteSourceImpl", "getStoreSales: $e")
            return Result.failure(e)
        }
    }

    override suspend fun getManagerSales(managerId: String): Result<Double> {
        try {
            val query = wholesalesCollection.where(
                Filter.and(
                    Filter.equalTo("distributorId", managerId),
                    Filter.equalTo("deleted", false)
                )
            )
            val aggregateQuery = query.aggregate(AggregateField.sum("totalAmount"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()

            val sum = result.get(AggregateField.sum("totalAmount"))
            Log.d("DashboardRemoteSourceImpl", "getManagerSales: $sum")
            return Result.success((sum as? Double)?.toDouble()?:0.0)

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getWholesaleSales(managerId: String): Result<Double> {
        try {
            val distributorQuery = wholesalesCollection.where(
                Filter.and(
                    Filter.notEqualTo("distributorId", managerId),
                    Filter.equalTo("deleted", false),
                    Filter.equalTo("productType", ProductType.PRODUCT)
                )
            )
            val distributorAggregateQuery = distributorQuery.aggregate(AggregateField.sum("totalAmount"))
            val distributorResult = distributorAggregateQuery.get(AggregateSource.SERVER).await()
            val ingotQuery = ingotTransactionsCollection.where(
                Filter.and(
                    Filter.notEqualTo("distributorId", managerId),
                    Filter.equalTo("deleted", false)
                )
            )
            val ingotAggregateQuery = ingotQuery.aggregate(AggregateField.sum("totalAmount"))
            val ingotResult = ingotAggregateQuery.get(AggregateSource.SERVER).await()

            val distributorResultSum = distributorResult.get(AggregateField.sum("totalAmount"))
            val ingotResultSum = ingotResult.get(AggregateField.sum("totalAmount"))
            val sum = (distributorResultSum as? Double)?.toDouble()?:0.0
            Log.d("DashboardRemoteSourceImpl", "getWholesaleSales dist: $sum")
            val sum2 =(ingotResultSum as? Double)?.toDouble()?:0.0
            Log.d("DashboardRemoteSourceImpl", "getWholesaleSales ing: $sum2")
            return Result.success((sum.plus(sum2)))
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getStoreLoss(): Result<Double> {
        try {
            val query = storeLossesCollection.whereEqualTo("deleted", false)
            val aggregateQuery = query.aggregate(AggregateField.sum("value"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()

            val sum = result.get(AggregateField.sum("value"))
            Log.d("DashboardRemoteSourceImpl", "getStoreLoss: $sum")
            return Result.success((sum as? Double)?.toDouble()?:0.0)

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getManagerLoss(): Result<Double> {
        try {
            val query = managerLossesCollection.whereEqualTo("deleted", false)
            val aggregateQuery = query.aggregate(AggregateField.sum("value"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()

            val sum = result.get(AggregateField.sum("value"))
            Log.d("DashboardRemoteSourceImpl", "getStoresProfits: $sum")

            return Result.success((sum as? Double)?.toDouble()?:0.0)

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getWholesaleLoss(): Result<Double> {
        try {
            val query = distributorLossesCollection.whereEqualTo("deleted", false)
            val aggregateQuery = query.aggregate(AggregateField.sum("value"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()
            val sum = result.get(AggregateField.sum("value"))
            Log.d("DashboardRemoteSourceImpl", "getStoresProfits: $sum")

            return Result.success((sum as? Double)?.toDouble()?:0.0)

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }
}