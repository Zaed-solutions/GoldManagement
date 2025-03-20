package com.zaed.manager.ui.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.data.model.category.CategoryWithInventory
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.SearchBar
import com.zaed.manager.ui.categories.components.CategoriesList
import com.zaed.manager.ui.categories.components.SaveCategoryBottomSheet
import org.koin.androidx.compose.koinViewModel

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    onShowNavDrawer: () -> Unit,
    viewModel: CategoriesViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CategoriesScreenContent(
        modifier = modifier,
        state = state,
        onAction = { action ->
            when(action){
                CategoriesUiAction.ShowNavDrawer -> onShowNavDrawer()
                else -> viewModel.handleAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreenContent(
    modifier: Modifier = Modifier,
    state: CategoriesUiState,
    onAction: (CategoriesUiAction) -> Unit
) {
    var selectedCategory by remember{
        mutableStateOf(CategoryWithInventory())
    }
    var isSaveCategorySheetVisible by remember{
        mutableStateOf(false)
    }
    var isConfirmDeleteSheetVisible by remember{
        mutableStateOf(false)
    }
    Scaffold (
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.products),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(CategoriesUiAction.ShowNavDrawer)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(bottom = 8.dp, end = 8.dp)
                    .rotate(45f),
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    selectedCategory = CategoryWithInventory()
                    isSaveCategorySheetVisible = true
                },
            ) {
                Icon(
                    modifier = Modifier.rotate(-45f),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Store"
                )
            }
        }
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            SearchBar(
                modifier = Modifier.fillMaxWidth().padding(vertical =8.dp, horizontal = 16.dp),
                query = state.searchQuery,
                onQueryChanged = {
                    onAction(CategoriesUiAction.UpdateSearchQuery(it))
                }
            )
            CategoriesList(
                modifier = Modifier.fillMaxSize(),
                isLoading = state.isLoading,
                categories = state.filteredCategoriesWithInventories,
                onEdit = {
                    selectedCategory = it
                    isSaveCategorySheetVisible = true
                },
                onDelete = {
                    selectedCategory = it
                    isConfirmDeleteSheetVisible = true
                }
            )
            SaveCategoryBottomSheet(
                visible = isSaveCategorySheetVisible,
                initialCategory = selectedCategory,
                onDismiss = {
                    isSaveCategorySheetVisible = false
                },
                onSave = {
                    isSaveCategorySheetVisible = false
                    onAction(
                        if(it.category.id.isBlank()) {
                            CategoriesUiAction.CreateCategory(it)
                        } else {
                            CategoriesUiAction.UpdateCategory(it)
                        }
                    )
                }
            )
            ConfirmDeleteBottomSheet(
                visible = isConfirmDeleteSheetVisible,
                onDismiss = {
                    isConfirmDeleteSheetVisible = false
                },
                onConfirm = {
                    isConfirmDeleteSheetVisible = false
                    onAction(CategoriesUiAction.DeleteCategory(selectedCategory))
                },
                label = stringResource(R.string.product)
            )
        }
    }
}