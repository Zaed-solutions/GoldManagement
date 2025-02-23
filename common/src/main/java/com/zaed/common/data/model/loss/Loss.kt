package com.zaed.common.data.model.loss

import com.zaed.common.data.model.authentication.ChangeLog
import java.util.Date

sealed class Loss {
    abstract val id: String
    abstract val value: Double
    abstract val reason: String
    abstract val date: Date
    abstract val userId: String
    abstract val userName: String
    abstract val deleted: Boolean
    abstract val logs: List<ChangeLog>
}