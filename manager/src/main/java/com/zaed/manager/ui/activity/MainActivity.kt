package com.zaed.manager.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.zaed.common.data.model.User
import com.zaed.common.data.model.UserApprovalStatus
import com.zaed.common.data.model.UserRole
import com.zaed.common.ui.auth.MainViewModel
import com.zaed.manager.app.navigation.NavigationHost
import com.zaed.manager.app.navigation.Route
import com.zaed.manager.ui.theme.ManagerTheme
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel = getViewModel<MainViewModel>()
            val state by mainViewModel.uiState.collectAsStateWithLifecycle()
            if (!state.loading) {
                ManagerTheme {
                    App(
                        localUser = state.currentUser
                    )
                }
            }
        }
    }
}

@Composable
fun App(
    modifier: Modifier = Modifier,
    localUser: User?
) {
    val startDestination = when {
        localUser == null -> Route.LoginRoute
        localUser.role != UserRole.MANAGER -> Route.LoginRoute
        localUser.approvalStatusType == UserApprovalStatus.APPROVED -> Route.HomeRoute
        else -> Route.LoginRoute
    }
    val navController = rememberNavController()
    NavigationHost(
        modifier = modifier,
        startDestination = startDestination,
        navController = navController
    )
}