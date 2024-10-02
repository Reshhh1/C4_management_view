package com.example.frontend_android.teams.presentation.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.frontend_android.R
import com.example.frontend_android.teams.data.remote.dtos.response.TeamSummary
import com.example.frontend_android.teams.presentation.overview.state.TeamOverViewState
import com.example.frontend_android.ui.components.ActionButton
import com.example.frontend_android.ui.components.OverviewRow
import com.example.frontend_android.ui.components.ShowError
import com.example.frontend_android.ui.components.wrappers.ContentWrapper
import com.example.frontend_android.ui.components.wrappers.WrapperActionIcon
import com.example.frontend_android.ui.widgets.CenteredLoadingIndicator

/**
 * Team overview screen
 * @author Reshwan Barhoe
 * @param viewModelState state of this screen
 * @param navigateToDashboard navigates to the dashboard
 * @param navigateToTeamCreate navigates to team create
 */
@Composable
fun TeamsOverviewScreen(
    viewModelState: TeamOverViewState,
    navigateToTeamCreate: () -> Unit,
    navigateToDashboard: () -> Unit
) {
    ContentWrapper(
        title = stringResource(id = R.string.dashboard_teams_button),
        topLeftIcons = {
            WrapperActionIcon(
                imageVector = Icons.Default.ArrowBackIosNew,
                onClick = navigateToDashboard
            )
        }
    ) {
        Container(viewModelState, navigateToTeamCreate)
    }
}

/**
 * Container of the screen, displays content
 * depending on the state
 * @author Reshwan Barhoe
 */
@Composable
private fun Container(
    teamState: TeamOverViewState,
    navigateToTeamCreate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TopSection()
        MiddleSection {
            when (teamState) {
                is TeamOverViewState.Success -> TeamList((teamState).teams)
                is TeamOverViewState.Error -> ShowError((teamState).errorMessage)
                is TeamOverViewState.Loading -> CenteredLoadingIndicator()
            }
        }
        LowerSection(navigateToTeamCreate)
    }
}

/**
 * Top section of the screen
 * @author Reshwan Barhoe
 */
@Composable
private fun TopSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp)
    ) {
    }
}

/**
 * Lower section of the screen
 * @author Reshwan Barhoe
 */
@Composable
private fun LowerSection(navigateToTeamCreate: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActionButton(
            onClick = navigateToTeamCreate,
            buttonName = stringResource(R.string.team_add)
        )
    }
}

/**
 * Middle section of the screen
 * @author Reshwan Barhoe
 * @param content composable child of this composable
 */
@Composable
private fun MiddleSection(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        content()
    }
}

/**
 * Colum where the teams are being displayed in.
 * Displays an error if the provided teams parameter
 * is a empty array
 * @author Reshwan barhoe
 * @param teams that are being displayed
 */
@Composable
private fun TeamList(teams: List<TeamSummary>) {
    if (teams.isEmpty()) {
        ShowError(message = stringResource(R.string.team_no_teams))
    } else {
        LazyColumn(
            modifier = Modifier
                .height(500.dp)
        ) {
            items(
                items = teams,
                key = { it.id }
            ) {
                OverviewRow(
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.medium_dimen)),
                    title = it.name
                )
            }
        }
    }
}