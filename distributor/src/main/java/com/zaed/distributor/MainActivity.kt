package com.zaed.distributor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.data.model.User
import com.zaed.common.data.model.UserApprovementStatusType
import com.zaed.common.data.model.UserRole
import com.zaed.common.ui.auth.MainViewModel
import com.zaed.distributor.app.navigation.NavigationHost
import com.zaed.distributor.app.navigation.Route
import com.zaed.distributor.ui.theme.DistributorAppTheme
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel = getViewModel<MainViewModel>()
            val state by mainViewModel.uiState.collectAsStateWithLifecycle()
            if(!state.loading) {
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
    val startDestination = when {
        localUser == null -> Route.Login
        localUser.role != UserRole.DISTRIBUTOR -> Route.Login
        localUser.approvementStatusType == UserApprovementStatusType.APPROVED -> Route.WholeSaleCustomers
        else -> Route.Login
    }
    NavigationHost(
        startDestination = startDestination
    )
}