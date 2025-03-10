package com.zaed.common.data.model.loss

import com.zaed.common.data.model.authentication.ChangeLog
import java.util.Date

data class ManagerLoss(
    override val id: String = "",
    override val value: Double = 0.0,
    override val reason: String = "",
    override val date: Date = Date(),
    override val userId: String = "",
    override val userName: String = "",
    override val deleted: Boolean = false,
    override val logs: List<ChangeLog> = emptyList(),
    val type: ManagerLossType = ManagerLossType.NORMAL,
): Loss()

enum class ManagerLossType {
    NORMAL,
    ALLOWANCE,
    PERSONAL_EXPENSE
}
