package com.zaed.manager.ui.usermanagement.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zaed.common.data.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementBottomSheet(
    modifier: Modifier = Modifier,
    isEditSheetVisible: Boolean,
    isDeleteSheetVisible: Boolean,
    selectedUser: User?,
    onDismissRequest: () -> Unit,
    onEditUser: (User) -> Unit,
    onDeleteUser: (String) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState()
    AnimatedVisibility(isEditSheetVisible || isDeleteSheetVisible) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = onDismissRequest,
        ) {
            AnimatedContent(isEditSheetVisible to isDeleteSheetVisible, label = "User Management Bottom Sheet") { state ->
                when{
                    state.first -> {
                        //todo: edit bottom sheet
                    }
                    state.second -> {
                        //todo: delete bottom sheet
                    }
                }
            }
        }
    }
}