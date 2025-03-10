package com.zaed.manager.app.navigation

import android.util.Log
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
import com.zaed.manager.ui.distributordetails.DistributorDetailsScreen
import com.zaed.manager.ui.distributors.DistributorsScreen
import com.zaed.manager.ui.distributorssales.DistributorsSalesScreen
import com.zaed.manager.ui.losses.LossesScreen
import com.zaed.manager.ui.storedetails.StoreDetailsScreen
import com.zaed.manager.ui.stores.StoresScreen
import com.zaed.manager.ui.storessales.StoresSalesScreen
import com.zaed.manager.ui.usermanagement.UserManagementScreen

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    startDestination: Route,
    navController: NavHostController,
    onShowNavDrawer: () -> Unit
) {
    NavHost (
        modifier= modifier,
        navController = navController,
        startDestination =startDestination,
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
                    navController.navigate(Route.UserManagementRoute){
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
        composable<Route.UserManagementRoute> {
            UserManagementScreen(
                onShowNavDrawer = onShowNavDrawer
            )
        }
        composable<Route.StoresRoute> {
            StoresScreen(
                onShowNavDrawer = onShowNavDrawer,
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
        composable<Route.DistributorsRoute> {
            DistributorsScreen(
                onShowNavDrawer = onShowNavDrawer,
                onNavigateToDistributorDetails = {
                    navController.navigate(Route.DistributorDetailsRoute(it))
                }
            )
        }
        composable<Route.DistributorDetailsRoute> {backstackEntry ->
            val distributorId = backstackEntry.toRoute<Route.DistributorDetailsRoute>().distributorId
            DistributorDetailsScreen(
                distributorId = distributorId,
                onNavigateToSaleDetails = {/*todo*/},
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
        composable<Route.StoresSalesRoute> {
            StoresSalesScreen(
                onShowNavDrawer = onShowNavDrawer,
                onNavigateToSaleDetails = {
                    /*TODO*/
                }
            )
        }
        composable<Route.DistributorsSalesRoute> {
            DistributorsSalesScreen(
                onShowNavDrawer = onShowNavDrawer,
                onNavigateToSaleDetails = {
                    /*TODO*/
                }
            )
        }
        composable<Route.LossesRoute> {
            LossesScreen(
                onShowNavDrawer = onShowNavDrawer
            )
        }
    }

}