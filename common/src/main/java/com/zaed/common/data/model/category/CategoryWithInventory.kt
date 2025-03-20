package com.zaed.common.data.model.category

import com.zaed.common.data.model.inventory.Inventory

data class CategoryWithInventory(
    val category: Category = Category(),
    val inventory: Inventory = Inventory(),
)
