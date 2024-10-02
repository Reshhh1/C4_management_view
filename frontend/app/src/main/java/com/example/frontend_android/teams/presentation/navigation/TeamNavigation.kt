package com.example.frontend_android.teams.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.frontend_android.teams.presentation.create.TeamCreateAddMemberScreen
import com.example.frontend_android.teams.presentation.create.TeamCreateScreen
import com.example.frontend_android.teams.presentation.create.TeamCreateViewModel
import com.example.frontend_android.teams.presentation.create.state.TeamCreateState
import com.example.frontend_android.teams.presentation.overview.TeamOverviewViewModel
import com.example.frontend_android.teams.presentation.overview.TeamsOverviewScreen
import com.example.frontend_android.ui.navigation.AppScreens
import com.example.frontend_android.users.data.remote.dtos.UserSummary


/**
 * Defines the possible backstack entries for the team
 * @author Reshwan Barhoe
 * @param navController navigation host
 * @param teamCreateViewModel viewmodel of the teamCreate
 */
fun NavGraphBuilder.teamGraph(
    navController: NavHostController,
    teamCreateViewModel: TeamCreateViewModel
) {
    teamCreateGraph(
        navController = navController,
        viewModel = teamCreateViewModel
    )
    teamOverviewGraph(
        navController = navController
    )
}

/**
 * Defines the possible backstack entries for the team create
 * @param navController navigation host
 * @param viewModel viewmodel of the teamCreate
 * @author Reshwan Barhoe
 */
private fun NavGraphBuilder.teamCreateGraph(
    navController: NavHostController,
    viewModel: TeamCreateViewModel
) {
    composable(AppScreens.TeamCreateAddMemberScreen.name) {
        TeamCreateAddMemberScreen(
            navigateToTeamCreate = {
                navController.navigate(AppScreens.TeamCreateScreen.name)
            },
            addSelectedMember = { viewModel.addSelectedMember(it) },
            removeSelectedMember = { viewModel.removeSelectedMember(it) },
            cancelSelectedMembers = { viewModel.cancelSelectedMembers() },
            createTeamCreateState = viewModel.teamCreateState,
            networkError = viewModel.networkError,
            onUserSearchTermChange = { viewModel.onUserSearchTermChange(it) },
            saveSelectedMembers = { viewModel.saveSelectedMembers() },
            userListState = viewModel.teamUserListState,
            isSelectedMembersValid = { viewModel.isSelectedMembersValid() }
        )
    }
    composable(AppScreens.TeamCreateScreen.name) {
        TeamCreateScreen(
            state = viewModel.teamCreateState,
            onSubmit = { viewModel.onSubmit() },
            networkError = viewModel.networkError,
            onTeamNameChange = { newValue: TeamCreateState ->
                viewModel.onTeamNameChange(
                    newValue
                )
            },
            navigateToTeamOverView = {
                navController.navigate(AppScreens.TeamOverviewScreen.name)
            },
            getUsersByName = { viewModel.getUsersByName() },
            navigateToAddMember = {
                navController.navigate((AppScreens.TeamCreateAddMemberScreen.name))
            },
            onMemberRemove = { member: UserSummary ->
                viewModel.removeMember(member)
            },
            resetState = { viewModel.resetState() }
        )
    }
}

/**
 * Defines the possible backstack entries for the team overview
 * @param navController navigation host
 * @author Reshwan Barhoe
 */
private fun NavGraphBuilder.teamOverviewGraph(
    navController: NavHostController
) {
    composable(AppScreens.TeamOverviewScreen.name) {
        val viewModel = hiltViewModel<TeamOverviewViewModel>()
        TeamsOverviewScreen(
            viewModelState = viewModel.teamState.value,
            navigateToTeamCreate = {
                navController.navigate(AppScreens.TeamCreateScreen.name)
            },
            navigateToDashboard = {
                navController.navigate(AppScreens.DashboardScreen.name)
            }
        )
    }
}