package com.zaed.common.domain.sale

import com.zaed.common.data.model.loss.DatedLoss
import com.zaed.common.data.model.sale.DatedSales
import com.zaed.common.data.model.sale.Sale
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format

class ConvertSalesToDatedSalesUseCase {
    operator fun invoke(
        sales: List<Sale>,
        format: DateFormat = DateFormat.DATE
    ): List<DatedSales> {
        return sales
            .sortedByDescending{ it.createdAt }
            .groupBy { it.createdAt.format(format) }
            .map { (formattedDate, sales) ->
                DatedSales(
                    formattedDate = formattedDate,
                    totalAmount = sales.sumOf { it.totalAmount },
                    sales = sales
                )
            }
            .toList()
    }
}