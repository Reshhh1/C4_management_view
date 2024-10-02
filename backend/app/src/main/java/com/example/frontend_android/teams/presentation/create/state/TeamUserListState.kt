package com.example.frontend_android.teams.presentation.create.state

import com.example.frontend_android.users.data.remote.dtos.UserSummary

/**
 * Sealed class to describe the possible states of the TeamUserList
 * @author Reshwan Barhoe
 */
sealed class TeamUserListState {
    data object Loading : TeamUserListState()
    data class Success(val users: List<UserSummary>) : TeamUserListState()
    data class Error(val errorMessage: String) : TeamUserListState()
}