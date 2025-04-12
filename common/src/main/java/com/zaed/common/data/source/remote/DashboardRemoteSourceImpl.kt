package com.zaed.common.data.source.remote

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.AggregateField
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.zaed.common.data.model.authentication.UserPermission
import com.zaed.common.data.model.dashboard.DateFilter
import com.zaed.common.data.repository.WholesaleDistributorSummary
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
    private val USERS_COLLECTION = "users"
    private val usersCollection = firestore.collection(USERS_COLLECTION)
    override suspend fun getStoresProfits(dateFilter: DateFilter): Result<Double> {
        try {
            Log.d("DashboardRemoteSourceImpl", "getStoresProfits: $dateFilter")
            val query = storeSalesCollection
                .whereEqualTo("deleted", false)
                .whereGreaterThanOrEqualTo("createdAt", dateFilter.startDate)
                .whereLessThan("createdAt", dateFilter.endDate)
            val aggregateQuery = query.aggregate(AggregateField.sum("profit"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()

            val sum = result.get(AggregateField.sum("profit"))
            Log.d("DashboardRemoteSourceImpl", "getStoresProfits: $sum")
            return Result.success((sum as? Double)?.toDouble() ?: 0.0)

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getWholesaleProfits(
        managerId: String,
        dateFilter: DateFilter
    ): Result<Double> {
        try {
            val distributorQuery = wholesalesCollection
                .where(
                    Filter.and(
                        Filter.notEqualTo("distributorId", managerId),
                        Filter.equalTo("deleted", false),
                        Filter.equalTo("productType", ProductType.PRODUCT),
                        Filter.greaterThanOrEqualTo("createdAt", dateFilter.startDate),
                        Filter.lessThan("createdAt", dateFilter.endDate)
                    )
                )
            val distributorAggregateQuery = distributorQuery.aggregate(AggregateField.sum("profit"))
            val distributorResult = distributorAggregateQuery.get(AggregateSource.SERVER).await()
            val ingotQuery = ingotTransactionsCollection.where(
                Filter.and(
                    Filter.notEqualTo("distributorId", managerId),
                    Filter.equalTo("deleted", false),
                    Filter.greaterThanOrEqualTo("createdAt", dateFilter.startDate),
                    Filter.lessThan("createdAt", dateFilter.endDate)
                )
            )
            val ingotAggregateQuery = ingotQuery.aggregate(AggregateField.sum("totalEarning"))
            val ingotResult = ingotAggregateQuery.get(AggregateSource.SERVER).await()

            val distributorResultSum = distributorResult.get(AggregateField.sum("profit"))
            val ingotResultSum = ingotResult.get(AggregateField.sum("totalEarning"))
            val sum = (distributorResultSum as? Double)?.toDouble() ?: 0.0
            Log.d("DashboardRemoteSourceImpl", "getWholesaleProfits dist: $sum")
            val sum2 = (ingotResultSum as? Double)?.toDouble() ?: 0.0
            Log.d("DashboardRemoteSourceImpl", "getWholesaleProfits ing: $sum2")
            return Result.success((sum.plus(sum2)))
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getManagerProfits(
        managerId: String,
        dateFilter: DateFilter
    ): Result<Double> {
        try {
            val query = wholesalesCollection
                .where(
                    Filter.and(
                        Filter.equalTo("distributorId", managerId),
                        Filter.equalTo("deleted", false),
                        Filter.greaterThanOrEqualTo("createdAt", dateFilter.startDate),
                        Filter.lessThan("createdAt", dateFilter.endDate)
                    )
                )
            val aggregateQuery = query.aggregate(AggregateField.sum("profit"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()

            val sum = result.get(AggregateField.sum("profit"))
            Log.d("DashboardRemoteSourceImpl", "getManagerProfits: $sum")
            return Result.success((sum as? Double)?.toDouble() ?: 0.0)

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getStoreSales(dateFilter: DateFilter): Result<Double> {
        try {
            val query = storeSalesCollection
                .whereEqualTo("deleted", false)
                .whereGreaterThanOrEqualTo("createdAt", dateFilter.startDate)
                .whereLessThan("createdAt", dateFilter.endDate)
            val aggregateQuery = query.aggregate(AggregateField.sum("totalAmount"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()

            val sum = result.get(AggregateField.sum("totalAmount"))
            Log.d("DashboardRemoteSourceImpl", "getStoreSales: $sum")
            return Result.success((sum as? Double)?.toDouble() ?: 0.0)

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            Log.d("DashboardRemoteSourceImpl", "getStoreSales: $e")
            return Result.failure(e)
        }
    }

    override suspend fun getManagerSales(
        managerId: String,
        dateFilter: DateFilter
    ): Result<Double> {
        try {
            val query = wholesalesCollection
                .where(
                    Filter.and(
                        Filter.equalTo("distributorId", managerId),
                        Filter.equalTo("deleted", false),
                        Filter.greaterThanOrEqualTo("createdAt", dateFilter.startDate),
                        Filter.lessThan("createdAt", dateFilter.endDate)
                    )
                )

            val aggregateQuery = query.aggregate(AggregateField.sum("totalAmount"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()

            val sum = result.get(AggregateField.sum("totalAmount"))
            Log.d("DashboardRemoteSourceImpl", "getManagerSales: $sum")
            return Result.success((sum as? Double)?.toDouble() ?: 0.0)

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getWholesaleSales(
        managerId: String,
        dateFilter: DateFilter
    ): Result<Double> {
        try {
            val distributorQuery = wholesalesCollection
                .where(
                    Filter.and(
                        Filter.notEqualTo("distributorId", managerId),
                        Filter.equalTo("deleted", false),
                        Filter.equalTo("productType", ProductType.PRODUCT),
                        Filter.greaterThanOrEqualTo("createdAt", dateFilter.startDate),
                        Filter.lessThan("createdAt", dateFilter.endDate)
                    )
                )
            val distributorAggregateQuery =
                distributorQuery.aggregate(AggregateField.sum("totalAmount"))
            val distributorResult = distributorAggregateQuery.get(AggregateSource.SERVER).await()
            val ingotQuery = ingotTransactionsCollection.where(
                Filter.and(
                    Filter.notEqualTo("distributorId", managerId),
                    Filter.equalTo("deleted", false),
                    Filter.greaterThanOrEqualTo("createdAt", dateFilter.startDate),
                    Filter.lessThan("createdAt", dateFilter.endDate)
                )
            )
            val ingotAggregateQuery = ingotQuery.aggregate(AggregateField.sum("totalAmount"))
            val ingotResult = ingotAggregateQuery.get(AggregateSource.SERVER).await()

            val distributorResultSum = distributorResult.get(AggregateField.sum("totalAmount"))
            val ingotResultSum = ingotResult.get(AggregateField.sum("totalAmount"))
            val sum = (distributorResultSum as? Double)?.toDouble() ?: 0.0
            Log.d("DashboardRemoteSourceImpl", "getWholesaleSales dist: $sum")
            val sum2 = (ingotResultSum as? Double)?.toDouble() ?: 0.0
            Log.d("DashboardRemoteSourceImpl", "getWholesaleSales ing: $sum2")
            return Result.success((sum.plus(sum2)))
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getStoreLoss(dateFilter: DateFilter): Result<Double> {
        try {
            val query = storeLossesCollection
                .whereEqualTo("deleted", false)
                .whereGreaterThanOrEqualTo("date", dateFilter.startDate)
                .whereLessThan("date", dateFilter.endDate)
            val aggregateQuery = query.aggregate(AggregateField.sum("value"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()

            val sum = result.get(AggregateField.sum("value"))
            Log.d("DashboardRemoteSourceImpl", "getStoreLoss: $sum")
            return Result.success((sum as? Double)?.toDouble() ?: 0.0)

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getManagerLoss(dateFilter: DateFilter): Result<Double> {
        try {
            Log.d("DashboardRemoteSourceImpl", "getManagerLoss: $dateFilter")
            val query = managerLossesCollection
                .whereEqualTo("deleted", false)
                .whereGreaterThanOrEqualTo("date", dateFilter.startDate)
                .whereLessThan("date", dateFilter.endDate)
            val aggregateQuery = query.aggregate(AggregateField.sum("value"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()
            val sum = result.get(AggregateField.sum("value"))
            Log.d("DashboardRemoteSourceImpl", "getManagerLoss: $sum")

            return Result.success((sum as? Double)?.toDouble() ?: 0.0)

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getWholesaleLoss(dateFilter: DateFilter): Result<Double> {
        try {
            val query = distributorLossesCollection
                .whereEqualTo("deleted", false)
                .whereGreaterThanOrEqualTo("date", dateFilter.startDate)
                .whereLessThan("date", dateFilter.endDate)
            val aggregateQuery = query.aggregate(AggregateField.sum("value"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()
            val sum = result.get(AggregateField.sum("value"))
            Log.d("DashboardRemoteSourceImpl", "getStoresProfits: $sum")

            return Result.success((sum as? Double)?.toDouble() ?: 0.0)

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getGoldSales(dateFilter: DateFilter): Result<Double> {
        try {
            val query = wholesalesCollection.where(
                Filter.and(
                    Filter.equalTo("deleted", false),
                    Filter.greaterThanOrEqualTo("createdAt", dateFilter.startDate),
                    Filter.lessThan("createdAt", dateFilter.endDate),
                    Filter.equalTo("productType", ProductType.GOLD)
                )
            )
            val aggregateQuery = query.aggregate(AggregateField.sum("totalAmount"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()
            val sum = result.get(AggregateField.sum("totalAmount"))
            Log.d("DashboardRemoteSourceImpl", "getGoldSales: $sum")

            return Result.success((sum as? Double)?.toDouble() ?: 0.0)

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getSilverSales(dateFilter: DateFilter): Result<Double> {
        try {
            val query = wholesalesCollection.where(
                Filter.and(
                    Filter.equalTo("deleted", false),
                    Filter.greaterThanOrEqualTo("createdAt", dateFilter.startDate),
                    Filter.lessThan("createdAt", dateFilter.endDate),
                    Filter.equalTo("productType", ProductType.SILVER)
                )
            )
            val aggregateQuery = query.aggregate(AggregateField.sum("totalAmount"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()
            val sum = result.get(AggregateField.sum("totalAmount"))
            Log.d("DashboardRemoteSourceImpl", "getSilverSales: $sum")

            return Result.success((sum as? Double)?.toDouble() ?: 0.0)

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getIngotTransactions(dateFilter: DateFilter): Result<Double> {
        try {
            val query = ingotTransactionsCollection.where(
                Filter.and(
                    Filter.equalTo("deleted", false),
                    Filter.greaterThanOrEqualTo("createdAt", dateFilter.startDate),
                    Filter.lessThan("createdAt", dateFilter.endDate),
                )
            )
            val aggregateQuery = query.aggregate(AggregateField.sum("totalEarning"))
            val result = aggregateQuery.get(AggregateSource.SERVER).await()
            val sum = result.get(AggregateField.sum("totalEarning"))
            Log.d("DashboardRemoteSourceImpl", "getIngotTransactions: $sum")

            return Result.success((sum as? Double)?.toDouble() ?: 0.0)

        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getProductDistributorSummary(dateFilter: DateFilter): Result<List<WholesaleDistributorSummary>> {
        try {
            val productUsers = usersCollection.whereArrayContains(
                "permissions",
                UserPermission.SELL_PRODUCTS
            ).get().await()
            val data = mutableListOf<WholesaleDistributorSummary>()
            productUsers.documents.forEach {
                val user = it.toObject(com.zaed.common.data.model.authentication.User::class.java)
                    ?: return@forEach
                val sales = wholesalesCollection.where(
                    Filter.and(
                        Filter.equalTo("distributorId", user.id),
                        Filter.equalTo("productType", ProductType.PRODUCT),
                        Filter.equalTo("deleted", false),
                        Filter.greaterThanOrEqualTo("createdAt", dateFilter.startDate),
                        Filter.lessThan("createdAt", dateFilter.endDate),
                    )
                )
                val losses = distributorLossesCollection.where(
                    Filter.and(
                        Filter.equalTo("userId", user.id),
                        Filter.equalTo("deleted", false),
                        Filter.greaterThanOrEqualTo("date", dateFilter.startDate),
                        Filter.lessThan("date", dateFilter.endDate),
                    )
                )
                val profit =
                    sales.aggregate(AggregateField.sum("profit")).get(AggregateSource.SERVER)
                        .await()
                val totalSales =
                    sales.aggregate(AggregateField.sum("totalAmount")).get(AggregateSource.SERVER)
                        .await()
                val loss = losses.aggregate(AggregateField.sum("value")).get(AggregateSource.SERVER)
                    .await()

                data.add(
                    WholesaleDistributorSummary(
                        profit = profit.get(AggregateField.sum("profit")) as? Double ?: 0.0,
                        sales = totalSales.get(AggregateField.sum("totalAmount")) as? Double ?: 0.0,
                        loss = loss.get(AggregateField.sum("value")) as? Double ?: 0.0,
                        type = ProductType.PRODUCT,
                        distributorName = user.fullName,
                        distributorId = user.id

                    )
                )
            }
            return Result.success(data)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getGoldDistributorSummary(dateFilter: DateFilter): Result<List<WholesaleDistributorSummary>> {
        try {
            val productUsers = usersCollection.whereArrayContains(
                "permissions",
                UserPermission.SELL_GOLD
            ).get().await()
            val data = mutableListOf<WholesaleDistributorSummary>()
            productUsers.documents.forEach {
                val user = it.toObject(com.zaed.common.data.model.authentication.User::class.java)
                    ?: return@forEach
                val sales = wholesalesCollection.where(
                    Filter.and(
                        Filter.equalTo("distributorId", user.id),
                        Filter.equalTo("deleted", false),
                        Filter.equalTo("productType", ProductType.GOLD),
                        Filter.greaterThanOrEqualTo("createdAt", dateFilter.startDate),
                        Filter.lessThan("createdAt", dateFilter.endDate),
                    )
                )
                val losses = distributorLossesCollection.where(
                    Filter.and(
                        Filter.equalTo("userId", user.id),
                        Filter.equalTo("deleted", false),
                        Filter.greaterThanOrEqualTo("date", dateFilter.startDate),
                        Filter.lessThan("date", dateFilter.endDate),
                    )
                )
                val profit =
                    sales.aggregate(AggregateField.sum("profit")).get(AggregateSource.SERVER)
                        .await()
                val totalSales =
                    sales.aggregate(AggregateField.sum("totalAmount")).get(AggregateSource.SERVER)
                        .await()
                val loss = losses.aggregate(AggregateField.sum("value")).get(AggregateSource.SERVER)
                    .await()

                data.add(
                    WholesaleDistributorSummary(
                        profit = profit.get(AggregateField.sum("profit")) as? Double ?: 0.0,
                        sales = totalSales.get(AggregateField.sum("totalAmount")) as? Double ?: 0.0,
                        loss = loss.get(AggregateField.sum("value")) as? Double ?: 0.0,
                        type = ProductType.PRODUCT,
                        distributorName = user.fullName,
                        distributorId = user.id
                    )
                )
            }
            return Result.success(data)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getSilverDistributorSummary(dateFilter: DateFilter): Result<List<WholesaleDistributorSummary>> {
        try {
            val productUsers = usersCollection.whereArrayContains(
                "permissions",
                UserPermission.SELL_SILVER
            ).get().await()
            val data = mutableListOf<WholesaleDistributorSummary>()
            productUsers.documents.forEach {
                val user = it.toObject(com.zaed.common.data.model.authentication.User::class.java)
                    ?: return@forEach
                val sales = wholesalesCollection.where(
                    Filter.and(
                        Filter.equalTo("distributorId", user.id),
                        Filter.equalTo("deleted", false),
                        Filter.equalTo("productType", ProductType.SILVER),
                        Filter.greaterThanOrEqualTo("createdAt", dateFilter.startDate),
                        Filter.lessThan("createdAt", dateFilter.endDate),
                    )
                )
                val losses = distributorLossesCollection.where(
                    Filter.and(
                        Filter.equalTo("userId", user.id),
                        Filter.equalTo("deleted", false),
                        Filter.greaterThanOrEqualTo("date", dateFilter.startDate),
                        Filter.lessThan("date", dateFilter.endDate),
                    )
                )
                val profit =
                    sales.aggregate(AggregateField.sum("profit")).get(AggregateSource.SERVER)
                        .await()
                val totalSales =
                    sales.aggregate(AggregateField.sum("totalAmount")).get(AggregateSource.SERVER)
                        .await()
                val loss = losses.aggregate(AggregateField.sum("value")).get(AggregateSource.SERVER)
                    .await()

                data.add(
                    WholesaleDistributorSummary(
                        profit = profit.get(AggregateField.sum("profit")) as? Double ?: 0.0,
                        sales = totalSales.get(AggregateField.sum("totalAmount")) as? Double ?: 0.0,
                        loss = loss.get(AggregateField.sum("value")) as? Double ?: 0.0,
                        type = ProductType.PRODUCT,
                        distributorName = user.fullName,
                        distributorId = user.id
                    )
                )
            }
            return Result.success(data)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun getIngotDistributorSummary(dateFilter: DateFilter): Result<List<WholesaleDistributorSummary>> {
        try {
            val productUsers = usersCollection.whereArrayContains(
                "permissions",
                UserPermission.SELL_INGOTS
            ).get().await()
            val data = mutableListOf<WholesaleDistributorSummary>()
            productUsers.documents.forEach {
                val user = it.toObject(com.zaed.common.data.model.authentication.User::class.java)
                    ?: return@forEach
                val sales = wholesalesCollection.where(
                    Filter.and(
                        Filter.equalTo("userId", user.id),
                        Filter.equalTo("deleted", false),
                        Filter.equalTo("productType", ProductType.INGOT),
                        Filter.greaterThanOrEqualTo("createdAt", dateFilter.startDate),
                        Filter.lessThan("createdAt", dateFilter.endDate),
                    )
                )
                val losses = distributorLossesCollection.where(
                    Filter.and(
                        Filter.equalTo("distributorId", user.id),
                        Filter.equalTo("deleted", false),
                        Filter.greaterThanOrEqualTo("date", dateFilter.startDate),
                        Filter.lessThan("date", dateFilter.endDate),
                    )
                )
                val profit =
                    sales.aggregate(AggregateField.sum("profit")).get(AggregateSource.SERVER)
                        .await()
                val totalSales =
                    sales.aggregate(AggregateField.sum("totalAmount")).get(AggregateSource.SERVER)
                        .await()
                val loss = losses.aggregate(AggregateField.sum("value")).get(AggregateSource.SERVER)
                    .await()

                data.add(
                    WholesaleDistributorSummary(
                        profit = profit.get(AggregateField.sum("profit")) as? Double ?: 0.0,
                        sales = totalSales.get(AggregateField.sum("totalAmount")) as? Double ?: 0.0,
                        loss = loss.get(AggregateField.sum("value")) as? Double ?: 0.0,
                        type = ProductType.PRODUCT,
                        distributorName = user.fullName,
                        distributorId = user.id
                    )
                )
            }
            return Result.success(data)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }
}