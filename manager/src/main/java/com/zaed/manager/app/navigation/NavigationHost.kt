package com.zaed.manager.app.navigation

import androidx.compose.compiler.plugins.kotlin.EmptyFunctionMetrics.composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.ui.auth.login.LoginScreen
import com.zaed.common.ui.auth.signup.SignUpScreen
import com.zaed.manager.ui.storedetails.StoreDetailsScreen
import com.zaed.manager.ui.stores.StoresScreen
import com.zaed.manager.ui.usermanagement.UserManagementScreen

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    startDestination: Route,
    navController: NavHostController
) {
    NavHost (
        navController = navController,
//        startDestination =startDestination,
        startDestination =Route.StoresRoute,
    ){
        composable<Route.LoginRoute> {
            LoginScreen(
                role = UserRole.MANAGER,
                onBack = {
                    navController.popBackStack()
                },
                navigateToSignUp = {
                    navController.navigate(Route.SignUpRoute)
                },
                onNavigateToHomeScreen = {
                    navController.navigate(Route.HomeRoute){
                        popUpTo(Route.LoginRoute){
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<Route.SignUpRoute> {
            SignUpScreen(
                role = UserRole.MANAGER,
                onBack = {
                    navController.popBackStack()
                },
                navigateToLogIn = {
                    navController.navigate(Route.LoginRoute)
                }
            )
        }
        composable<Route.HomeRoute> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        navController.navigate(Route.UserManagementRoute)
                    }
                ) {
                    Text(text = "Go to User Management")
                }
            }
        }
        composable<Route.UserManagementRoute> {
            UserManagementScreen()
        }
        composable<Route.StoresRoute> {
            StoresScreen(
                onShowNavDrawer = {
//                    TODO()
                },
                onNavigateToStoreDetails = {
                    navController.navigate(Route.StoreDetailsRoute(it))
                }
            )
        }
        composable<Route.StoreDetailsRoute> { backstackEntry ->
            val storeId = backstackEntry.toRoute<Route.StoreDetailsRoute>().storeId
            StoreDetailsScreen(
                storeId = storeId,
                onNavigateToSaleDetails = {/*todo*/},
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }
    }

}