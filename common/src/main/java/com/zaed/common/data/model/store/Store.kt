package com.zaed.common.data.model.store

import com.zaed.common.data.model.DropdownMenuItem

data class Store(
    val id: String = "",
    override val name: String = "",
    val location: String = "",
    val createdAt: String = "",
): DropdownMenuItem {
    override fun toString(): String {
        return name
    }
}
