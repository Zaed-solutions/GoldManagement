package com.zaed.common.data.model

data class User(
    val id : String = "",
    val fullName :String = "",
    val userName: String = "",
    val password: String = "",
    val approvementStatusType: UserApprovementStatusType = UserApprovementStatusType.PENDING,
    val role: UserRole = UserRole.NONE
)


