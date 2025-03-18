package com.zaed.manager.ui.losses

import com.zaed.common.data.model.loss.ManagerLoss
import com.zaed.common.ui.util.DateFormat
import java.util.Date

sealed interface LossesUiAction {
    data object OnShowNavDrawer: LossesUiAction
    data class UpdateLossesDateFilter(val filter: DateFormat): LossesUiAction
    data class SetCustomLossesRange(val range: Pair<Date?, Date?>): LossesUiAction
    data class UpdatePersonalExpensesDateFilter(val filter: DateFormat): LossesUiAction
    data class SetCustomPersonalExpensesRange(val range: Pair<Date?, Date?>): LossesUiAction
    data class SaveLoss(val loss: ManagerLoss): LossesUiAction
    data class DeleteLoss(val loss: ManagerLoss): LossesUiAction

}