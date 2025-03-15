package com.zaed.common.domain.sale

import com.zaed.common.data.model.sale.DatedSales
import com.zaed.common.data.model.sale.Transaction
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format

class ConvertSalesToDatedSalesUseCase {
    operator fun invoke(
        transactions: List<Transaction>,
        format: DateFormat = DateFormat.DATE
    ): List<DatedSales> {
        return transactions
            .sortedByDescending{ it.createdAt }
            .groupBy { it.createdAt.format(format) }
            .map { (formattedDate, sales) ->
                DatedSales(
                    formattedDate = formattedDate,
                    totalAmount = sales.sumOf { it.totalAmount },
                    transactions = sales
                )
            }
            .toList()
    }
}