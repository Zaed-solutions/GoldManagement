package com.zaed.distributor.ui.losses

import com.zaed.common.data.model.loss.DistributorLoss
import com.zaed.common.data.model.loss.StoreLoss
import com.zaed.common.ui.util.DateFormat
import java.util.Date

sealed interface LossesUiAction {
    data object OnShowNavDrawer : LossesUiAction
    data class OnAddLoss(
        val loss: DistributorLoss,
    ) : LossesUiAction
    data class OnUpdateLoss(
        val loss: DistributorLoss,
    ) : LossesUiAction
    data class OnDeleteLoss(
        val loss : DistributorLoss
    ) : LossesUiAction
    data class UpdateSelectedDateFilter(val filter: DateFormat) : LossesUiAction
    data class SetCustomRangeFilter(val range: Pair<Date?, Date?>): LossesUiAction
}