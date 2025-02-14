package com.zaed.common.ui.util

sealed class LossError(
    errorType: LossErrorType,
) : BaseError(errorType.userMessage) {
    data class NetworkLoss(
        override val location: String = "",
        val errorType: LossErrorType
    ): LossError(LossErrorType.NETWORK_FAILURE)
}


