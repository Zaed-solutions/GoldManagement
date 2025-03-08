package com.zaed.cashier

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zaed.cashier.app.navigation.NavDrawerItem
import com.zaed.cashier.app.navigation.NavigationHost
import com.zaed.cashier.app.navigation.Route
import com.zaed.cashier.ui.theme.CashierAppTheme
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.authentication.UserApprovalStatus
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.ui.auth.MainViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
                        localUser = state.currentUser,
                        onSignOut = {
                            mainViewModel.signOut()
                        },
                        isSignedOut = state.isSignedOut
                    )
                }
            }
        }
    }
}

@Composable
fun App(
    localUser: User?,
    isSignedOut: Boolean = false,
    onSignOut: () -> Unit = {},
) {
    val startDestination = when {
        localUser?.isApprovedCashier() == true -> Route.AddSaleRoute()
        else -> Route.LoginRoute
    }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val mutex = remember { Mutex() }
    val navDrawerRoutes = remember { NavDrawerItem.entries.map { it.route::class.qualifiedName } }
    val currentRoute = (navBackStackEntry?.destination?.route
        ?: startDestination::class.qualifiedName.orEmpty()).substringBefore("?")
    Log.d("EdzaDebug", "App: $navDrawerRoutes, $currentRoute")
    LaunchedEffect(isSignedOut) {
        if (isSignedOut) {
            navController.navigate(Route.LoginRoute) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = navDrawerRoutes.contains(currentRoute),
        drawerContent = {
            ModalDrawerSheet {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(com.zaed.common.R.drawable.app_icon),
                        contentDescription = "App Logo",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(82.dp)
                    )
                    Text(
                        text = stringResource(R.string.app_name),
                        modifier = Modifier.padding(start = 16.dp),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                }
                Text(
                    stringResource(
                        com.zaed.common.R.string.logged_in_as_placeholder,
                        localUser?.fullName ?: ""
                    ),
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .padding(start = 16.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = 8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    NavDrawerItem.entries.forEach { item ->
                        NavigationDrawerItem(
                            shape = RoundedCornerShape(0.dp),
                            modifier = Modifier.padding(vertical = 8.dp),
                            label = { Text(text = stringResource(item.title)) },
                            selected = currentRoute == item.route::class.qualifiedName.orEmpty(),
                            icon = item.icon?.let {
                                {
                                    Icon(
                                        painter = painterResource(it),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            },
                            onClick = {
                                scope.launch {
                                    mutex.withLock {
                                        navController.navigate(item.route) {
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                        drawerState.close()
                                    }
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
                    NavigationDrawerItem(
                        shape = RoundedCornerShape(0.dp),
                        modifier = Modifier.padding(vertical = 8.dp),
                        label = { Text(text = stringResource(com.zaed.common.R.string.sign_out)) },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedTextColor = MaterialTheme.colorScheme.error,
                            unselectedIconColor = MaterialTheme.colorScheme.error,
                        ),
                        onClick = {
                            onSignOut()
                            scope.launch {
                                drawerState.close()
                            }
                        },
                    )

                }
            }
        },
    ) {
        NavigationHost(
            onShowNavDrawer = {
                scope.launch {
                    mutex.withLock {
                        drawerState.open()
                    }
                }
            },
            startDestination = startDestination,
            navController = navController
        )
    }
}



fun User.isApprovedCashier(): Boolean =
    role == UserRole.CASHIER && approvalStatusType == UserApprovalStatus.APPROVED