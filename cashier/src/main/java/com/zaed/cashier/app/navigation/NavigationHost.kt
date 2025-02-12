package com.zaed.cashier.app.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zaed.common.ui.component.auth.login.LoginScreen
import com.zaed.common.ui.component.auth.signup.SignUpScreen
import kotlinx.serialization.Serializable

@Composable
fun NavigationHost(
    startDestination: Any
) {
    val navController = rememberNavController()
    NavHost (
        navController = navController,
        startDestination =startDestination,
    ){
        composable<Route.SignUp> {
            SignUpScreen(
                onBack = {
                    navController.popBackStack()
                },
                navigateToLogIn = {
                    navController.navigate(Route.Login)
                },
                onNavigateToPendingScreen = {
                    navController.navigate(Route.Pending){
                        popUpTo(Route.SignUp){
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<Route.Login> {
            LoginScreen(
                onBack = {
                    navController.popBackStack()
                },
                navigateToSignUp = {
                    navController.navigate(Route.SignUp)
                },
                onNavigateToPendingScreen = {
                    navController.navigate(Route.Pending){
                        popUpTo(Route.Login){
                            inclusive = true
                        }
                    }
                },
                onNavigateToHomeScreen = {
                    navController.navigate(Route.Home){
                        popUpTo(Route.Login){
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<Route.Pending> {
            PendingScreen(

            )
        }
        composable<Route.Home> {
            HomeScreen()
        }

    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Home",
            style = MaterialTheme.typography.titleLarge
        )
    }}

@Composable
fun PendingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Pending",
            style = MaterialTheme.typography.titleLarge
        )
    }
}



sealed interface Route{
    @Serializable
    data object SignUp: Route
    @Serializable
    data object Login: Route
    @Serializable
    data object Pending: Route
    @Serializable
    data object Home: Route



}