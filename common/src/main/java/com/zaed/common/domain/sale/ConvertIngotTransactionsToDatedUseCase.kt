package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.DatedIngotTransactions
import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format

class ConvertIngotTransactionsToDatedUseCase {
    operator fun invoke(transactions: List<IngotTransaction>, dateFormat: DateFormat): List<DatedIngotTransactions>{
        return transactions
            .sortedByDescending { it.createdAt }
            .groupBy { it.createdAt.format(dateFormat) }
            .map { (formattedDate, transactions) ->
                DatedIngotTransactions(
                    formattedDate = formattedDate,
                    totalEarnings = transactions.sumOf { it.totalEarning },
                    transactions = transactions
                )
            }
            .toList()
    }
}