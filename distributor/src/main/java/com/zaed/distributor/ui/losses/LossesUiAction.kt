package com.zaed.distributor.ui.losses

import com.zaed.common.data.model.loss.DistributorLoss
import com.zaed.common.data.model.loss.StoreLoss

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
}