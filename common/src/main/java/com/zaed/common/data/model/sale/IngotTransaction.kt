package com.zaed.common.data.model.sale

import androidx.annotation.StringRes
import com.zaed.common.R
import com.zaed.common.data.model.authentication.ChangeLog
import kotlinx.serialization.Transient
import java.util.Date

data class IngotTransaction(
    val id: String = "",
    val createdAt: Date = Date(),
    val distributorId: String = "",
    val distributorName: String = "",
    val logs: List<ChangeLog> = emptyList(),
    val deleted: Boolean = false,
    val grams: Double = 0.0,
    val sellingGramPrice: Double = 0.0,
    val buyingGramPrice: Double = 0.0,
    val karat: Karat = Karat.K18,
    val type: TransactionType = TransactionType.SALE,
) {
    @Transient
    val totalEarning
        get() = (sellingGramPrice - buyingGramPrice) * grams

    @Transient
    val totalAmount
        get() = when(type){
            TransactionType.PURCHASE -> buyingGramPrice * grams
            TransactionType.SALE -> sellingGramPrice * grams
        }
}
enum class TransactionType(@StringRes val stringRes: Int) {
    PURCHASE(R.string.purchase),
    SALE(R.string.sale)
}
enum class Karat (val value: Int) {
    K24(24),
    K18(18),
}
