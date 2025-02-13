package com.zaed.cashier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.cashier.app.navigation.NavigationHost
import com.zaed.cashier.app.navigation.Route
import com.zaed.cashier.ui.theme.CashierAppTheme
import com.zaed.common.data.model.User
import com.zaed.common.data.model.UserApprovementStatusType
import com.zaed.common.data.model.UserRole
import com.zaed.common.ui.auth.MainViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel = getViewModel<MainViewModel>()
            val state by mainViewModel.uiState.collectAsStateWithLifecycle()
            if(!state.loading) {
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
        localUser.approvementStatusType == UserApprovementStatusType.APPROVED -> Route.SalesRoute
        else -> Route.LoginRoute
    }
    NavigationHost(
        startDestination = startDestination
    )
}


