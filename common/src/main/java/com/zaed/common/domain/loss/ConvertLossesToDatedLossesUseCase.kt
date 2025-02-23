package com.zaed.common.domain.loss

import com.zaed.common.data.model.loss.DatedLoss
import com.zaed.common.data.model.loss.Loss
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format

class ConvertLossesToDatedLossesUseCase {
    operator fun invoke(losses: List<Loss>): List<DatedLoss> {
        return losses
            .sortedByDescending{ it.date }
            .groupBy { it.date.format(DateFormat.DATE) }
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