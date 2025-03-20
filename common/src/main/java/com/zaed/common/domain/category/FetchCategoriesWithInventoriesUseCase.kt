package com.zaed.common.domain.category

import android.util.Log
import com.zaed.common.data.model.authentication.UserPermission
import com.zaed.common.data.model.category.Category
import com.zaed.common.data.model.category.CategoryWithInventory
import com.zaed.common.data.model.inventory.Inventory
import com.zaed.common.data.model.inventory.request.FetchInventoriesRequest
import com.zaed.common.data.repository.CategoryRepository
import com.zaed.common.data.repository.InventoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class FetchCategoriesWithInventoriesUseCase(
    private val categoryRepo: CategoryRepository,
    private val inventoryRepo: InventoryRepository
) {
    operator fun invoke(): Flow<Result<List<CategoryWithInventory>>> = callbackFlow {
        try {
            var latestCategories: List<Category> = emptyList()
            var latestInventories: List<Inventory> = emptyList()
            val combineAndSend = {
                val result = mutableListOf<CategoryWithInventory>()
                val categoryMap = latestCategories.associateBy { it.id }
                for (inventory in latestInventories) {
                    val categoryId = inventory.productId
                    val category = categoryMap[categoryId]
                    if (category != null) {
                        val categoryWithInventory = CategoryWithInventory(
                            category = category,
                            inventory = inventory
                        )
                        result.add(categoryWithInventory)
                    }
                }
                Log.d("EdreesDebug", "invoke: ${result}")
                trySend(Result.success(result))
            }
            launch(Dispatchers.IO) {
                categoryRepo.fetchAllCategories().collect { result ->
                    result.onSuccess {
                        Log.d("EdreesDebug", "invoke: fetchAllCategories: ${it}")
                        latestCategories = it
                        combineAndSend()
                    }.onFailure {
                        trySend(Result.failure(it))
                    }
                }
            }
            launch(Dispatchers.IO) {
                inventoryRepo.fetchInventories(
                    FetchInventoriesRequest(
                        ownerId = "",
                        permissions = listOf(UserPermission.SELL_PRODUCTS)
                    )
                ).collect { result ->
                    result.onSuccess {
                        Log.d("EdreesDebug", "invoke: fetchAllInventories: ${it}")
                        latestInventories = it
                        combineAndSend()
                    }.onFailure {
                        trySend(Result.failure(it))
                    }
                }
            }
        } catch (e: Exception) {
            trySend(Result.failure(e))
        }
        awaitClose { }
    }
}
