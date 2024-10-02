package com.example.frontend_android.c4_model.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.frontend_android.c4_model.presentation.C4OverviewViewModel
import com.example.frontend_android.c4_model.presentation.C4Screen
import com.example.frontend_android.ui.navigation.AppScreens

/**
 * Defines the possible backstack entries for the c4 model
 * @param navController navigation host
 * @author Reshwan Barhoe
 */
fun NavGraphBuilder.c4Graph(
    navController: NavHostController
) {
    composable(AppScreens.C4ModelScreen.name) {
        val viewModel = hiltViewModel<C4OverviewViewModel>()
        C4Screen(
            c4ModelUiState = viewModel.c4ModelUiState.value,
            c4ModelState = viewModel.c4ModelState.value,
            navigateToDashboard = {
                navController.navigate(AppScreens.DashboardScreen.name)
            },
            toggleElementExpansionById = { viewModel.toggleElementExpansionById(it) },
            isRowExpanded = { viewModel.isRowExpanded(it) },
            getContainersByContextId = { viewModel.getContainersByContextId(it) }
        )
    }
}