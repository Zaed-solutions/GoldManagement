package com.zaed.common.domain.loss

import com.zaed.common.data.model.loss.Loss
import com.zaed.common.data.model.loss.request.GetStoreLossesRequest
import com.zaed.common.data.repository.LossRepository
import com.zaed.common.ui.util.DateFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Locale

class GetStoreLossesUseCase(
    private val lossRepository: LossRepository
) {
    operator fun invoke(request: GetStoreLossesRequest): Flow<Result<Map<String, List<Loss>>>>{
        return lossRepository.getStoreLosses(request).map {
            it.map { it.toLossesGroups() }
        }
    }
    private fun List<Loss>.toLossesGroups(): Map<String, List<Loss>> {
        val dateFormatter = SimpleDateFormat(DateFormat.DATE.pattern, Locale.getDefault())
        return groupBy { loss ->
            dateFormatter.format(loss.date)
        }
    }
}
