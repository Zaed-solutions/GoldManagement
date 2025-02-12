package com.zaed.manager.ui.usermanagement.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zaed.common.data.model.User
import com.zaed.common.ui.components.ConfirmDeleteDialog
import com.zaed.manager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementBottomSheet(
    modifier: Modifier = Modifier,
    isEditBottomSheetVisible: Boolean,
    isDeleteBottomSheetVisible: Boolean,
    selectedUser: User?,
    onDismissRequest: () -> Unit,
    onEditUser: (User) -> Unit,
    onDeleteUser: (String) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState()
    AnimatedVisibility(isEditBottomSheetVisible || isDeleteBottomSheetVisible) {
        ModalBottomSheet(
            modifier = modifier,
            sheetState = bottomSheetState,
            onDismissRequest = onDismissRequest,
        ) {
            AnimatedContent(isEditBottomSheetVisible to isDeleteBottomSheetVisible, label = "User Management Bottom Sheet") { state ->
                when{
                    state.first -> {
                        EditUserSheetContent(
                            selectedUser = selectedUser?:User(),
                            onDismissRequest = onDismissRequest,
                            onEditUser = onEditUser
                        )
                    }
                    state.second -> {
                        ConfirmDeleteDialog(
                            label = stringResource(R.string.user),
                            onDismiss = onDismissRequest,
                            onConfirm = {
                                onDeleteUser(selectedUser?.id ?: "")
                            }
                        )
                    }
                }
            }
        }
    }
}