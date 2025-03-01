package com.zaed.common.domain.loss

import com.zaed.common.data.model.loss.DatedLoss
import com.zaed.common.data.model.loss.Loss
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format

class ConvertLossesToDatedLossesUseCase {
    operator fun invoke(losses: List<Loss>, format: DateFormat = DateFormat.DATE): List<DatedLoss> {
        return losses
            .sortedByDescending{ it.date }
            .groupBy { it.date.format(format) }
            .map { (formattedDate, losses) ->
                DatedLoss(
                    formattedDate = formattedDate,
                    totalLosses = losses.sumOf { it.value },
                    losses = losses
                )
            }
            .toList()
    }
}