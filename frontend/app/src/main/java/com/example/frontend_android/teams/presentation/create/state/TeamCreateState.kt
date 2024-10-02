package com.example.frontend_android.teams.presentation.create.state

import com.example.frontend_android.users.data.remote.dtos.UserSummary

/**
 * The state of the TeamCreate
 * @author Reshwan Barhoe
 */
data class TeamCreateState(
    val name: String = "",
    val nameError: String = "",
    val members: List<UserSummary> = listOf(),
    val memberError: String = "",
    val selectedMembers: List<UserSummary> = listOf(),
    val selectedMemberError: String = "",
    val userSearchTerm: String = "",
    var successfullyCreated: Boolean = false
)