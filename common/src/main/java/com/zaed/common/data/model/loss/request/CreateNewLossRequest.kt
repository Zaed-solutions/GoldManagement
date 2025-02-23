package com.zaed.common.data.model.loss.request

import com.zaed.common.data.model.loss.StoreLoss

data class CreateNewLossRequest(
    val loss: StoreLoss,
)