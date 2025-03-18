package com.zaed.common.data.source.remote

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
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
    override suspend fun addPayment(request: AddNewPaymentRequest): Result<String> {
        try {
            Log.d("PaymentRemoteDataSource", "addPayment: ${request.payment}")
            val document = moneyPaymentsCollection.document()
            when(request.payment){
                is CashPayment->{
                    document.set(
                        request.payment.copy(id = document.id, customerId = request.customerId)
                    ).await()
                }
                is BankTransferPayment->{
                    document.set(
                        request.payment.copy(id = document.id, customerId = request.customerId)
                    ).await()
                }
                is ChequePayment->{
                    document.set(
                        request.payment.copy(id = document.id, customerId = request.customerId)
                    ).await()
                }
                is FuturePayment->{
                    document.set(
                        request.payment.copy(id = document.id, customerId = request.customerId)
                    ).await()
                }
                is LossPayment->{
                    document.set(
                        request.payment.copy(id = document.id, customerId = request.customerId)
                    ).await()
                }
                is GoldPayment->{
                    document.set(
                        request.payment.copy(id = document.id, customerId = request.customerId)
                    ).await()
                }
                is ManagerCheque->{
                    document.set(
                        request.payment.copy(id = document.id, customerId = request.customerId)
                    ).await()
                }
            }
            customersCollection.document(request.customerId).update(
                "debtAmount",
                FieldValue.increment(request.payment.signedAmount()),
            ).await()
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
                        Filter.equalTo("customerId", request.customerId)
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
                                PaymentType.FUTURES -> it.toObject(FuturePayment::class.java)
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
                    PaymentType.FUTURES -> it.toObject(FuturePayment::class.java)
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

    override fun fetchSupplierPayments(request: FetchSupplierPaymentsRequest): Flow<Result<List<Payment>>>
    = callbackFlow {
        try{
            val filter = if(request.isManager) {
                Filter.equalTo("customerId", request.supplierId)
            } else {
                Filter.and(
                    Filter.equalTo("customerId", request.supplierId),
                    Filter.equalTo("deleted", false)
                )
            }
            moneyPaymentsCollection.where(
                filter
            ).addSnapshotListener{ snapshot, error ->
                if(error != null){
                    crashlytics.recordException(error)
                    trySend(Result.failure(error))
                } else {
                    val payments = snapshot?.mapNotNull {
                        val type = it.toObject(CashPayment::class.java).type
                        when (type) {
                            PaymentType.CASH -> it.toObject(CashPayment::class.java)
                            PaymentType.BANK_TRANSFER -> it.toObject(BankTransferPayment::class.java)
                            PaymentType.CHEQUE -> it.toObject(ChequePayment::class.java)
                            PaymentType.FUTURES -> it.toObject(FuturePayment::class.java)
                            PaymentType.LOSS -> it.toObject(LossPayment::class.java)
                            PaymentType.GOLD -> it.toObject(GoldPayment::class.java)
                            PaymentType.MANAGER_CHEQUES -> it.toObject(ManagerCheque::class.java)
                        }
                    } ?: emptyList()
                    trySend(Result.success(payments))
                }
            }
        } catch (e: Exception){
            crashlytics.recordException(e)
            trySend(Result.failure(e))
        }
        awaitClose {  }
    }

    override suspend fun editPayment(request: EditPaymentRequest): Result<Unit> {
        try {
            moneyPaymentsCollection.document(request.oldPayment.id).set(request.newPayment)
                .await()
            customersCollection.document(request.customerId).update(
                "debtAmount",
                FieldValue.increment(request.diff),
            ).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun deletePayment(request: DeletePaymentRequest): Result<Unit> {
        try {
            moneyPaymentsCollection.document(request.payment.id).update(
                mapOf(
                    "deleted" to true,
                    "logs" to FieldValue.arrayUnion(
                        ChangeLog(
                            employeeId = request.employeeId,
                            employeeName = request.employeeName,
                            type = LogType.DELETE
                        )
                    )
                )
            ).await()
            customersCollection.document(request.payment.customerId).update(
                "debtAmount",
                FieldValue.increment(request.payment.signedAmount().unaryMinus()),
            ).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            crashlytics.recordException(e)
            e.printStackTrace()
            return Result.failure(e)
        }
    }
}