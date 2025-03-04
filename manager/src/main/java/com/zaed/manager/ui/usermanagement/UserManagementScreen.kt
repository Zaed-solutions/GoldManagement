package com.zaed.manager.ui.usermanagement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.data.model.authentication.User
import com.zaed.common.ui.components.SearchBar
import com.zaed.common.R
import com.zaed.manager.ui.theme.ManagerTheme
import com.zaed.manager.ui.usermanagement.components.RejectsList
import com.zaed.manager.ui.usermanagement.components.RequestsList
import com.zaed.manager.ui.usermanagement.components.UserManagementBottomSheet
import com.zaed.manager.ui.usermanagement.components.UserManagementTab
import com.zaed.manager.ui.usermanagement.components.UserManagementTopBar
import com.zaed.manager.ui.usermanagement.components.UsersList
import com.zaed.manager.ui.usermanagement.components.UsersTabRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserManagementScreen(
    modifier: Modifier = Modifier,
    viewModel: UserManagementViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    UserManagementScreenContent(
        onAction = { viewModel.handleAction(it) },
        state = state
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserManagementScreenContent(
    modifier: Modifier = Modifier,
    onAction: (UserManagementUiAction) -> Unit = {},
    state: UserManagementUiState = UserManagementUiState(),
    scope: CoroutineScope = rememberCoroutineScope()
) {
    var isEditBottomSheetVisible by remember { mutableStateOf(false) }
    var isDeleteBottomSheetVisible by remember { mutableStateOf(false) }
    var selectedUser: User? by remember { mutableStateOf(null) }
    val pagerState = rememberPagerState(pageCount = { UserManagementTab.entries.size })
    Scaffold (
        modifier = modifier,
        topBar = {
            UserManagementTopBar()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            //search bar
            SearchBar(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                query = state.searchQuery,
                placeHolder = stringResource(R.string.search_users),
                onQueryChanged = { onAction(UserManagementUiAction.UpdateSearchQuery(it)) }
            )
            //tab row
            UsersTabRow(
                selectedIndex = pagerState.currentPage,
                onTabSelected = { index ->
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
            //horizontal pager
            HorizontalPager(
                state = pagerState
            ) { page ->
                when(page){
                    UserManagementTab.Users.ordinal -> {
                        //users list
                        UsersList(
                            users = state.displayedUsers,
                            onEditClicked = {
                                selectedUser = it
                                isEditBottomSheetVisible = true
                            },
                            onRevokeAccessClicked = {
                                onAction(UserManagementUiAction.ChangeApprovedStatus(it.id, false))
                            },
                            onDeleteClicked = {
                                selectedUser = it
                                isDeleteBottomSheetVisible = true
                            }
                        )
                    }
                    UserManagementTab.Requests.ordinal -> {
                        //requests list
                        RequestsList(
                            requests = state.displayedRequests,
                            onRespondToRequest = { userId, isAccepted ->
                                onAction(UserManagementUiAction.ChangeApprovedStatus(userId, isAccepted))
                            }
                        )
                    }
                    UserManagementTab.Rejects.ordinal -> {
                        RejectsList(
                            rejects = state.displayedRejects,
                            onGrantAccessClicked = {
                                onAction(UserManagementUiAction.ChangeApprovedStatus(it, true))
                            },
                            onDeleteClicked = {
                                selectedUser = it
                                isDeleteBottomSheetVisible = true
                            }
                        )
                    }
                }
            }
            UserManagementBottomSheet(
                isEditBottomSheetVisible = isEditBottomSheetVisible,
                isDeleteBottomSheetVisible = isDeleteBottomSheetVisible,
                selectedUser = selectedUser,
                onDismissRequest = {
                    isEditBottomSheetVisible = false
                    isDeleteBottomSheetVisible = false
                },
                onDeleteUser = {
                    onAction(UserManagementUiAction.DeleteUser(it))
                    isDeleteBottomSheetVisible = false
                },
                onEditUser = {
                    onAction(UserManagementUiAction.UpdateUser(it))
                    isEditBottomSheetVisible = false
                }
            )
        }
    }

}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun Preview() {
    ManagerTheme {
        UserManagementScreenContent()
    }
}