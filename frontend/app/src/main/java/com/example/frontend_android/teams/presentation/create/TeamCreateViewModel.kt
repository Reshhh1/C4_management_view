package com.example.frontend_android.teams.presentation.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend_android.teams.domain.errors.TeamResource
import com.example.frontend_android.teams.domain.use_cases.CreateTeamUseCase
import com.example.frontend_android.teams.presentation.create.state.TeamCreateState
import com.example.frontend_android.teams.presentation.create.state.TeamUserListState
import com.example.frontend_android.users.data.remote.dtos.UserSummary
import com.example.frontend_android.users.domain.use_cases.SearchUserUseCase
import com.example.frontend_android.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamCreateViewModel @Inject constructor(
    private val createTeamUseCase: CreateTeamUseCase,
    private val searchUserUseCase: SearchUserUseCase
) : ViewModel() {

    var teamUserListState: TeamUserListState by mutableStateOf(
        TeamUserListState.Loading
    )
        private set
    var teamCreateState by mutableStateOf(TeamCreateState())
        private set

    var networkError by mutableStateOf("")

    /**
     * Changes by updating and validating the state
     * @author Reshwan Barhoe
     * @param teamCreateState new state
     */
    fun onTeamNameChange(teamCreateState: TeamCreateState) {
        this.teamCreateState = teamCreateState
        validateAndUpdateTeamNameErrorState()
    }

    /**
     * Updates and validates the search term state
     * A request will be sent if validation is successful
     * @author Reshwan Barhoe
     */
    fun onUserSearchTermChange(searchTerm: String) {
        teamCreateState = teamCreateState.copy(
            userSearchTerm = searchTerm
        )
        val validationResult = searchUserUseCase.getSearchTermValidationResults(searchTerm)
        if (validationResult.successful) {
            getUsersByName()
        } else {
            teamUserListState = TeamUserListState.Success(listOf())
        }
    }

    /**
     * Launches a view model scoped coroutine, makes use of the domain layer to perform
     * the correct usecase and handles the response of it.
     * @author Reshwan Barhoe
     */
    private fun createTeam() {
        viewModelScope.launch {
            when (val result = createTeamUseCase.createTeam(teamCreateState)) {
                is TeamResource.Success -> handleTeamCreationSuccess()
                is TeamResource.NameAlreadyExists -> result.message?.let {
                    teamCreateState = teamCreateState.copy(
                        nameError = it
                    )
                }

                is TeamResource.NetworkError -> result.message?.let {
                    networkError = it
                }
            }
        }
    }

    /**
     * Launches a view model scoped coroutine, makes use of the domain layer to perform
     * the correct usecase and handles the response of it.
     * There is a delay 500ms to minimize the amount of requests
     * @author Reshwan Barhoe
     */
    fun getUsersByName() {
        viewModelScope.launch {
            delay(500)
            when (val result =
                searchUserUseCase.getUsersBySearchTerm(teamCreateState.userSearchTerm)) {
                is Resource.Success -> {
                    result.data?.let {
                        teamUserListState = TeamUserListState.Success(result.data)
                    }
                }

                is Resource.Error -> result.message?.let {
                    networkError = it
                }
            }
        }
    }

    /**
     * Adds a user to the selectedMembers property state
     * @param user that's being added
     *
     * @author Reshwan barhoe
     */
    fun addSelectedMember(user: UserSummary) {
        if (createTeamUseCase.isValidSelectedMember(teamCreateState, user.id)) {
            teamCreateState = teamCreateState.copy(
                selectedMembers = teamCreateState.selectedMembers.plus(user),
                selectedMemberError = ""
            )
        } else {
            validateAndUpdateSelectedMembers()
        }
    }

    /**
     * Removes a user of the selectedMembers property state
     * @param user that's being removed
     * @author Reshwan barhoe
     */
    fun removeSelectedMember(user: UserSummary) {
        teamCreateState = teamCreateState.copy(
            selectedMembers = teamCreateState.selectedMembers.minus(user),
            selectedMemberError = ""
        )
        validateAndUpdateSelectedMembers()
    }

    /**
     * Removes a member of the member property state
     * @param user that's being added
     * @author Reshwan Barhoe
     */
    fun removeMember(user: UserSummary) {
        teamCreateState = teamCreateState.copy(
            members = teamCreateState.members.minus(user),
            selectedMembers = teamCreateState.selectedMembers.minus(user)
        )
        validateAndUpdateTeamMemberErrorState()
    }

    /**
     * Method that reverts the selected members to its original state
     * this method is being used upon clicking the cancel button
     * @author Reshwan Barhoe
     */
    fun cancelSelectedMembers() {
        teamCreateState = teamCreateState.copy(
            selectedMembers = teamCreateState.members
        )
    }

    /**
     * Method that saves the selected members as member in the staet
     * It also validates and updates the state to display any errors
     * @author Reshwan Barhoe
     */
    fun saveSelectedMembers() {
        teamCreateState = teamCreateState.copy(
            members = teamCreateState.selectedMembers
        )
    }

    /**
     * Method that resets the state, but sets the successfullyCreated property
     * to true.
     * This makes the client navigate to another screen
     * @author Reshwan Barhoe
     */
    private fun handleTeamCreationSuccess() {
        teamCreateState = TeamCreateState(successfullyCreated = true)
    }

    /**
     * Validates and updates the state of team name
     * @author Reshwan Barhoe
     */
    private fun validateAndUpdateTeamNameErrorState() {
        val validationResults = createTeamUseCase.getTeamNameValidationResult(teamCreateState)
        teamCreateState = teamCreateState.copy(
            nameError = validationResults.errorMessage
        )
    }

    /**
     * Validates a updates the team member error state
     * @author Reshwan Barhoe
     */
    private fun validateAndUpdateTeamMemberErrorState() {
        val validationResult = createTeamUseCase.getMemberValidationResult(teamCreateState)
        teamCreateState = teamCreateState.copy(
            memberError = validationResult.errorMessage
        )
    }

    /**
     * Validates and returns if the state is valid
     * @author Reshwan Barhoe
     */
    fun isSelectedMembersValid(): Boolean {
        validateAndUpdateSavingSelectedMembers()
        return teamCreateState.selectedMemberError.isEmpty()
    }

    /**
     * Validates and updates the selected member error state
     * @author Reshwan Barhoe
     */
    private fun validateAndUpdateSavingSelectedMembers() {
        val validationResult =
            createTeamUseCase.getSavingSelectedMemberValidationResult(teamCreateState)
        teamCreateState = teamCreateState.copy(
            memberError = "",
            selectedMemberError = validationResult.errorMessage
        )
    }

    /**
     * Validates and updates the selected member error state
     * @author Reshwan Barhoe
     */
    private fun validateAndUpdateSelectedMembers() {
        val validationResult = createTeamUseCase.getSelectedMemberValidationResult(teamCreateState)
        teamCreateState = teamCreateState.copy(
            selectedMemberError = validationResult.errorMessage
        )
    }

    /**
     * Checks for any field errors
     * @author Reshwan Barhoe
     * @return if the state errors contain a value
     */
    private fun containsError(): Boolean {
        return teamCreateState.nameError.isNotEmpty() || teamCreateState.memberError.isNotEmpty()
    }

    /**
     * Resets the states
     * @author Reshwan Barhoe
     */
    fun resetState() {
        teamCreateState = TeamCreateState()
        teamUserListState = TeamUserListState.Loading
    }

    /**
     * Validates the states
     * Updates any error states so that could be displayed in the screen
     * Checks if the state contains a error, if they don't a team will be created
     * @author Reshwan Barhoe
     */
    fun onSubmit() {
        validateAndUpdateTeamNameErrorState()
        validateAndUpdateTeamMemberErrorState()
        val containsErrors = containsError()
        if (!containsErrors) {
            createTeam()
        }
    }
}