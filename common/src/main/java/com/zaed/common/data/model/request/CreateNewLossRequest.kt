package com.zaed.common.data.model.request

data class CreateNewLossRequest(
    val value : Double = 0.0,
    val reason : String = "",
)