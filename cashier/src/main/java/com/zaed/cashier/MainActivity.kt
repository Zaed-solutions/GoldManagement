package com.zaed.cashier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zaed.cashier.app.navigation.BottomNavigationBar
import com.zaed.cashier.app.navigation.BottomNavigationItem
import com.zaed.cashier.app.navigation.NavigationHost
import com.zaed.cashier.app.navigation.Route
import com.zaed.cashier.ui.theme.CashierAppTheme
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.authentication.UserApprovalStatus
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.ui.auth.MainViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel = getViewModel<MainViewModel>()
            val state by mainViewModel.uiState.collectAsStateWithLifecycle()
            if (!state.loading) {
                CashierAppTheme {
                    App(
                        localUser = state.currentUser
                    )
                }
            }
        }
    }
}

@Composable
fun App(localUser: User?) {
    val startDestination = when {
        localUser == null -> Route.LoginRoute
        localUser.role != UserRole.CASHIER -> Route.LoginRoute
        localUser.approvalStatusType == UserApprovalStatus.APPROVED -> Route.SalesRoute
        else -> Route.LoginRoute
    }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = (navBackStackEntry?.destination?.route
        ?: Route.SalesRoute::class.qualifiedName.orEmpty()).substringBefore("?")
    val visibleRoutes = BottomNavigationItem.entries.map { it.route::class.qualifiedName.orEmpty() }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
        ,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            AnimatedVisibility(
                visible = visibleRoutes.contains(currentRoute),
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
            ) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route)
                    }
                )

            }
        }
    ) { innerPadding ->
        NavigationHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = startDestination
        )
    }
}


