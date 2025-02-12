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
import com.zaed.cashier.ui.MainViewModel
import com.zaed.common.data.model.User
import com.zaed.common.data.model.UserApprovementStatusType
import com.zaed.common.ui.theme.GoldManagementTheme
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel = getViewModel<MainViewModel>()
            val state by mainViewModel.uiState.collectAsStateWithLifecycle()
            if(!state.loading) {
                GoldManagementTheme {
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
    NavigationHost(
        startDestination = when (localUser?.approvementStatusType) {
            UserApprovementStatusType.PENDING -> Route.Pending
            UserApprovementStatusType.APPROVED -> Route.Home
            UserApprovementStatusType.REJECTED -> Route.Login
            null -> Route.Login
        }
    )
}


