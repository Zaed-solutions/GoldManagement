package com.zaed.cashier.ui.loss

import com.zaed.common.data.model.loss.StoreLoss

sealed interface LossUiAction {
    data class OnCreateLoss(
        val loss: StoreLoss,
    ) : LossUiAction
    data class OnUpdateLoss(
        val loss: StoreLoss,
    ) : LossUiAction
    data class OnDeleteLoss(
        val id : String = ""
    ) : LossUiAction
    data object ResetError : LossUiAction
    data object ResetSuccess : LossUiAction
    data object OnSignOut : LossUiAction
}