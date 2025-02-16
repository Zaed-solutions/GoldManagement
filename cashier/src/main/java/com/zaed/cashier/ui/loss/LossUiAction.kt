package com.zaed.cashier.ui.loss

import com.zaed.common.data.model.Loss

sealed interface LossUiAction {
    data class OnCreateLoss(
        val loss: Loss,
    ) : LossUiAction
    data class OnUpdateLoss(
        val loss: Loss,
    ) : LossUiAction
    data class OnDeleteLoss(
        val id : String = ""
    ) : LossUiAction
    data object ResetError : LossUiAction
    data object ResetSuccess : LossUiAction
    data object OnSignOut : LossUiAction
}