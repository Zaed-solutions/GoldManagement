package com.zaed.common.data.model.authentication

data class User(
    val storeId: String = "",
    val storeName: String = "",
    val id : String = "",
    val fullName :String = "",
    val userName: String = "",
    val password: String = "",
    val approvalStatusType: UserApprovalStatus = UserApprovalStatus.PENDING,
    val role: UserRole = UserRole.NONE,
    val permissions: List<UserPermission>  = emptyList()
)


