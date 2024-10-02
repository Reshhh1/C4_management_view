package com.example.frontend_android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.frontend_android.auth.presentation.login.LoginViewModel
import com.example.frontend_android.auth.presentation.navigation.loginGraph
import com.example.frontend_android.c4_model.presentation.navigation.c4Graph
import com.example.frontend_android.teams.presentation.create.TeamCreateViewModel
import com.example.frontend_android.teams.presentation.navigation.teamGraph
import com.example.frontend_android.users.presentation.RegistrationViewModel
import com.example.frontend_android.users.presentation.navigation.registrationGraph

/**
 * Navigation API that's being used to navigate the client
 * to other screens
 * @author Reshwan Barhoe
 * @param startDestination the starting destination
 */
@Composable
fun AppNavigation(
    startDestination: AppScreens = AppScreens.LoginScreen
) {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val registrationViewModel: RegistrationViewModel = hiltViewModel()
    val teamCreateViewModel = hiltViewModel<TeamCreateViewModel>()
    NavHost(
        navController = navController,
        startDestination = startDestination.name
    ) {
        teamGraph(
            navController = navController,
            teamCreateViewModel = teamCreateViewModel
        )
        registrationGraph(
            viewModel = registrationViewModel,
            navController = navController
        )
        loginGraph(
            viewModel = loginViewModel,
            navController = navController
        )
        c4Graph(
            navController = navController
        )
    }
}