package com.zaed.common.data.source.remote

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.authentication.LogType
import com.zaed.common.data.model.cheque.ManagerCheque
import com.zaed.common.data.model.payment.BankTransferPayment
import com.zaed.common.data.model.payment.CashPayment
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.FuturePayment
import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.LossPayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.payment.request.AddNewPaymentRequest
import com.zaed.common.data.model.payment.request.DeletePaymentRequest
import com.zaed.common.data.model.payment.request.EditPaymentRequest
import com.zaed.common.data.model.payment.request.FetchCustomerPaymentsRequest
import com.zaed.common.data.model.payment.request.FetchPaymentsByIdsRequest
import com.zaed.common.data.model.payment.request.FetchSupplierPaymentsRequest
import com.zaed.common.data.model.payment.signedAmount
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PaymentRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashlytics: FirebaseCrashlytics
) : PaymentRemoteDataSource {
    private val moneyPaymentsCollection = firestore.collection("payments")
    private val customersCollection = firestore.collection("whole_sale_customers")
    private val suppliersCollection = firestore.collection("suppliers")
    override suspend fun addPayment(request: AddNewPaymentRequest): Result<String> {
        try {
            Log.d("PaymentRemoteDataSource", "addPayment: ${request.payment}")
            val batch = firestore.batch()
            val document = moneyPaymentsCollection.document()
            when (request.payment) {
                is CashPayment -> {
                    batch.set(
                        document,
                        request.payment.copy(id = document.id, accountId = request.accountId)
                    )
                }

                is BankTransferPayment -> {
                    batch.set(
                        document,
                        request.payment.copy(id = document.id, accountId = request.accountId)
                    )
                }

                is ChequePayment -> {
                    batch.set(
                        document,
                        request.payment.copy(id = document.id, accountId = request.accountId)
                    )
                }

                is FuturePayment -> {
                    batch.set(
                        document,
                        request.payment.copy(id = document.id, accountId = request.accountId)
                    )
                }

                is LossPayment -> {
                    batch.set(
                        document,
                        request.payment.copy(id = document.id, accountId = request.accountId)
                    )
                }

                is GoldPayment -> {
                    batch.set(
                        document,
                        request.payment.copy(id = document.id, accountId = request.accountId)
                    )
                }

                is ManagerCheque -> {
                    batch.set(
                        document,
                        request.payment.copy(id = document.id, accountId = request.accountId)
                    )
                }
            }
            if (request.isSupplier) {
                val docRef = suppliersCollection.document(request.accountId)
                batch.update(
                    docRef,
                    "moneyDebtAmount",
                    FieldValue.increment(request.payment.signedAmount().unaryMinus())
                )
            } else {
                val docRef = customersCollection.document(request.accountId)
                batch.update(
                    docRef,
                    "moneyDebtAmount",
                    FieldValue.increment(request.payment.signedAmount())
                )
            }
            batch.commit().await()
            return Result.success(document.id)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            return Result.failure(e)
        }
    }

    override fun fetchCustomerPayments(request: FetchCustomerPaymentsRequest): Flow<Result<List<Payment>>> =
        callbackFlow {
            try {
                moneyPaymentsCollection.where(
                    Filter.and(
                        Filter.equalTo("deleted", false),
                        Filter.equalTo("accountId", request.customerId),
                        Filter.or(
                            Filter.equalTo("type", PaymentType.FUTURES),
                            Filter.equalTo("type", PaymentType.REMAIN),
                        )
                    )
                ).addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        crashlytics.recordException(error)
                        trySend(Result.failure(error))
                    } else {
                        val payments = snapshot?.mapNotNull {
                            val type = it.toObject(CashPayment::class.java).type
                            when (type) {
                                PaymentType.CASH -> it.toObject(CashPayment::class.java)
                                PaymentType.BANK_TRANSFER -> it.toObject(BankTransferPayment::class.java)
                                PaymentType.CHEQUE -> it.toObject(ChequePayment::class.java)
                                PaymentType.FUTURES, PaymentType.REMAIN -> it.toObject(FuturePayment::class.java)
                                PaymentType.LOSS -> it.toObject(LossPayment::class.java)
                                PaymentType.GOLD -> it.toObject(GoldPayment::class.java)
                                PaymentType.MANAGER_CHEQUES -> it.toObject(ManagerCheque::class.java)
                            }
                        } ?: emptyList()
                        trySend(Result.success(payments))
                    }
                }
            } catch (e: Exception) {
                crashlytics.recordException(e)
                e.printStackTrace()
                trySend(Result.failure(e))
            }
            awaitClose { }
        }

    override suspend fun fetchMoneyPaymentsByIds(request: FetchPaymentsByIdsRequest): Result<List<Payment>> {
        return try {
            val result = moneyPaymentsCollection.where(
                Filter.and(
                    Filter.equalTo("deleted", false),
                    Filter.inArray("id", request.paymentsIds)
                )
            ).get().await()
            val payments = result.map {
                val type = it.toObject(CashPayment::class.java).type
                when (type) {
                    PaymentType.CASH -> it.toObject(CashPayment::class.java)
                    PaymentType.BANK_TRANSFER -> it.toObject(BankTransferPayment::class.java)
                    PaymentType.CHEQUE -> it.toObject(ChequePayment::class.java)
                    PaymentType.FUTURES, PaymentType.REMAIN -> it.toObject(FuturePayment::class.java)
                    PaymentType.LOSS -> it.toObject(LossPayment::class.java)
                    PaymentType.GOLD -> it.toObject(GoldPayment::class.java)
                    PaymentType.MANAGER_CHEQUES -> it.toObject(ManagerCheque::class.java)
                }
            }
            Result.success(payments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchGoldPaymentsByIds(request: FetchPaymentsByIdsRequest): Result<List<GoldPayment>> {
        return try {
            val moneyPayments =
                moneyPaymentsCollection.whereIn("id", request.paymentsIds).get().await()
                    .toObjects<GoldPayment>()
            Result.success(moneyPayments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun fetchSupplierPayments(request: FetchSupplierPaymentsRequest): Flow<Result<List<Payment>>> =
        callbackFlow {
            try {
                val filter = if (request.isManager) {
//                Filter.equalTo("customerId", request.supplierId)
                    Filter.and(
                        Filter.equalTo("accountId", request.supplierId),
                        Filter.equalTo("deleted", false)
                    )
                } else {
                    Filter.and(
                        Filter.equalTo("accountId", request.supplierId),
                        Filter.equalTo("deleted", false)
                    )
                }
                moneyPaymentsCollection.where(
                    filter
                ).orderBy("createdAt", Query.Direction.DESCENDING)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            crashlytics.recordException(error)
                            trySend(Result.failure(error))
                        } else {
                            val payments = snapshot?.mapNotNull {
                                val type = it.toObject(CashPayment::class.java).type
                                when (type) {
                                    PaymentType.CASH -> it.toObject(CashPayment::class.java)
                                    PaymentType.BANK_TRANSFER -> it.toObject(BankTransferPayment::class.java)
                                    PaymentType.CHEQUE -> it.toObject(ChequePayment::class.java)
                                    PaymentType.FUTURES, PaymentType.REMAIN -> it.toObject(
                                        FuturePayment::class.java
                                    )

                                    PaymentType.LOSS -> it.toObject(LossPayment::class.java)
                                    PaymentType.GOLD -> it.toObject(GoldPayment::class.java)
                                    PaymentType.MANAGER_CHEQUES -> it.toObject(ManagerCheque::class.java)
                                }
                            } ?: emptyList()
                            trySend(Result.success(payments))
                        }
                    }
            } catch (e: Exception) {
                crashlytics.recordException(e)
                trySend(Result.failure(e))
            }
            awaitClose { }
        }

    override suspend fun editPayment(request: EditPaymentRequest): Result<Unit> {
        try {
            val batch = firestore.batch()
            batch.set(
                moneyPaymentsCollection.document(request.oldPayment.id),
                request.newPayment
            )
            batch.update(
                if (request.isSupplier) {
                    suppliersCollection.document(request.accountId)
                } else {
                    customersCollection.document(request.accountId)
                },
                mapOf(
                    "moneyDebtAmount" to FieldValue.increment(request.diff.let { if (request.isSupplier) it.unaryMinus() else it })
                )
            )
            batch.commit().await()
            return Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun deletePayment(request: DeletePaymentRequest): Result<Unit> {
        try {
            val batch = firestore.batch()
            batch.set(
                moneyPaymentsCollection.document(request.payment.id),
                mapOf(
                    "deleted" to true,
                    "logs" to FieldValue.arrayUnion(
                        ChangeLog(
                            employeeId = request.employeeId,
                            employeeName = request.employeeName,
                            type = LogType.DELETE
                        )
                    )
                ),
            )
            batch.update(
                if (request.isSupplier) {
                    suppliersCollection.document(request.payment.accountId)
                } else {
                    customersCollection.document(request.payment.accountId)
                },
                mapOf(
                    "moneyDebtAmount" to FieldValue.increment(
                        request.payment.signedAmount()
                            .let { if (request.isSupplier) it else it.unaryMinus() })
                )
            )
            batch.commit().await()
            return Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }
}