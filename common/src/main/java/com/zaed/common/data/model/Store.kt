package com.zaed.common.data.model

data class Store(
    val id: String = "",
    override val name: String = ""
): DropdownMenuItem {
    override fun toString(): String {
        return name
    }
}
