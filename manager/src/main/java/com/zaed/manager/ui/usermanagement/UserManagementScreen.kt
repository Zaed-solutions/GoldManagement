package com.zaed.manager.ui.usermanagement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.ui.components.SearchBar
import com.zaed.manager.R
import com.zaed.manager.ui.theme.GoldManagementTheme
import com.zaed.manager.ui.usermanagement.components.UserManagementTab
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


}

@Composable
private fun UserManagementScreenContent(
    modifier: Modifier = Modifier,
    onAction: (UserManagementUiAction) -> Unit = {},
    state: UserManagementUiState = UserManagementUiState(),
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val pagerState = rememberPagerState(pageCount = { UserManagementTab.entries.size })
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            //search bar
            SearchBar(
                modifier = Modifier.padding(16.dp),
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
                        )
                    }
                    UserManagementTab.Requests.ordinal -> {
                        //requests list
                    }
                }
            }
        }
    }

}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun Preview() {
    GoldManagementTheme {
        UserManagementScreenContent()
    }
}