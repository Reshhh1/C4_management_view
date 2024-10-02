package com.example.frontend_android.auth.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.frontend_android.auth.presentation.dashboard.DashboardScreen
import com.example.frontend_android.auth.presentation.login.LoginScreen
import com.example.frontend_android.auth.presentation.login.LoginViewModel
import com.example.frontend_android.ui.navigation.AppScreens

/**
 * Defines the possible backstack entries for the login
 * @param navController navigation host
 * @param viewModel viewmodel of the login
 * @author Reshwan Barhoe
 */
fun NavGraphBuilder.loginGraph(
    viewModel: LoginViewModel,
    navController: NavHostController
) {
    composable(AppScreens.LoginScreen.name) {
        LoginScreen(
            navigateToDashboard = {
                navController.navigate(AppScreens.DashboardScreen.name)
            },
            viewModel = viewModel,
            navigateToFullName = {
                navController.navigate(AppScreens.RegisterFullNameScreen.name)
            }
        )
    }
    composable(AppScreens.DashboardScreen.name) {
        DashboardScreen(
            viewModel = viewModel,
            navigateToTeamOverview = {
                navController.navigate(AppScreens.TeamOverviewScreen.name)
            },
            navigateToC4OverView = {
                navController.navigate(AppScreens.C4ModelScreen.name)
            }
        )
    }
}