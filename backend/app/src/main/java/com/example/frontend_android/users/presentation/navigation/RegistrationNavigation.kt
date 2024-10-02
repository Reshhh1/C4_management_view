package com.example.frontend_android.users.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.frontend_android.ui.navigation.AppScreens
import com.example.frontend_android.users.presentation.RegistrationViewModel
import com.example.frontend_android.users.presentation.screens.emailScreen.EmailScreen
import com.example.frontend_android.users.presentation.screens.fullNameScreen.FullNameScreen
import com.example.frontend_android.users.presentation.screens.passwordScreen.PasswordScreen


/**
 * Defines the possible backstack entries for the registration
 * @author Reshwan Barhoe
 * @param navController navigation host
 * @param viewModel viewmodel of the registration
 */
fun NavGraphBuilder.registrationGraph(
    viewModel: RegistrationViewModel,
    navController: NavHostController
) {
    composable(AppScreens.RegisterFullNameScreen.name) {
        FullNameScreen(registrationViewModel = viewModel, onNext = {
            navController.navigate(AppScreens.RegisterEmailScreen.name)
        })
    }

    composable(AppScreens.RegisterEmailScreen.name) {
        EmailScreen(registrationViewModel = viewModel, onNext = {
            navController.navigate(AppScreens.RegisterPasswordScreen.name)
        }, goBack = {
            navController.navigateUp()
        })
    }

    composable(AppScreens.RegisterPasswordScreen.name) {
        PasswordScreen(registrationViewModel = viewModel, navigateToLogin = {
            navController.navigate(AppScreens.LoginScreen.name)
        }, goBack = {
            navController.navigateUp()
        },
            navigateBackToEmail = {
                navController.navigateUp()
            })
    }
}