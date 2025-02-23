package com.zaed.distributor.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
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
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.authentication.UserApprovalStatus
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.ui.auth.MainViewModel
import com.zaed.distributor.R
import com.zaed.distributor.app.navigation.NavigationHost
import com.zaed.distributor.app.navigation.Route
import com.zaed.distributor.app.navigation.mapToNavDrawerItems
import com.zaed.distributor.ui.theme.DistributorAppTheme
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
                DistributorAppTheme {
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
    val startDestination = remember(localUser) {
        when {
            localUser?.isApprovedDistributor() == true -> Route.SalesRoute
            else -> Route.LoginRoute
        }
    }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val mutex = remember { Mutex() }
    val navDrawerItems = remember(localUser?.permissions) {
        localUser?.permissions?.mapToNavDrawerItems() ?: emptyList()
    }
    val navDrawerRoutes =
        remember(navDrawerItems) { navDrawerItems.map { it.route::class.qualifiedName.orEmpty() } }
    val currentRoute = (navBackStackEntry?.destination?.route
        ?: startDestination::class.qualifiedName.orEmpty()).substringBefore("?")
    LaunchedEffect(localUser?.permissions) {
        if (!navDrawerItems.any { it.route::class.qualifiedName.orEmpty() == currentRoute }) {
            navController.navigate(startDestination) {
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = "App Logo",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(250.dp)
                    )

                }
                HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
                Column(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    navDrawerItems.forEach { item ->
                        NavigationDrawerItem(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            label = { Text(text = stringResource(item.title)) },
                            selected = currentRoute == item.route::class.qualifiedName.orEmpty(),
                            icon = item.icon?.let {
                                {
                                    Icon(
                                        painter = painterResource(it),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            },
                            shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
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

fun User.isApprovedDistributor(): Boolean =
    role == UserRole.DISTRIBUTOR && approvalStatusType == UserApprovalStatus.APPROVED
