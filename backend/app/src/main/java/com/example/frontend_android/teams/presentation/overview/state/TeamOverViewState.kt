package com.example.frontend_android.teams.presentation.overview.state

import com.example.frontend_android.teams.data.remote.dtos.response.TeamSummary

/**
 * The state management of the TeamOverView
 * @author Reshwan Barhoe
 */
sealed class TeamOverViewState {
    data object Loading : TeamOverViewState()
    data class Success(val teams: List<TeamSummary>) : TeamOverViewState()
    data class Error(val errorMessage: String) : TeamOverViewState()
}