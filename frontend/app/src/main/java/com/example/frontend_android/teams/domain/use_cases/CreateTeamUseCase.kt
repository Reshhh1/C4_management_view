package com.example.frontend_android.teams.domain.use_cases

import com.example.frontend_android.auth.domain.use_case.ValidationResult
import com.example.frontend_android.teams.data.repository.TeamRepository
import com.example.frontend_android.teams.domain.errors.TeamResource
import com.example.frontend_android.teams.domain.model.TeamModel
import com.example.frontend_android.teams.presentation.create.state.TeamCreateState
import com.example.frontend_android.util.ResponseId
import javax.inject.Inject

class CreateTeamUseCase @Inject constructor(
    private val teamRepository: TeamRepository
) {

    /**
     * Gets the validation results of the team name. This is being done to give specific
     * error results which could be handled in the presentation layer for input-specific error messages
     * @author Reshwan Barhoe
     * @param teamCreateState the state for creating a team
     * @return ValidationResult of the team name
     */
    fun getTeamNameValidationResult(teamCreateState: TeamCreateState): ValidationResult {
        val teamModel = TeamModel(teamCreateState.name, teamCreateState.members.map { it.id })
        return teamModel.validateTeamName()
    }

    /**
     * Validates the selected member state
     * @author Reshwan Barhoe
     * @param teamCreateState that contains the required information
     * @return if the selected member is valid to add
     */
    fun isValidSelectedMember(teamCreateState: TeamCreateState, userId: Int): Boolean {
        val teamModel =
            TeamModel(teamCreateState.name, teamCreateState.selectedMembers.map { it.id })
        return (teamModel.isWithinMaxMemberBoundaries() && !teamModel.isMemberIdPresent(userId))
    }

    /**
     * Gets the validation results of the team member. This is being done to give specific
     * error results which could be handled in the presentation layer for input-specific error messages
     * @author Reshwan Barhoe
     * @param teamCreateState the state for creating a team
     * @return ValidationResult
     */
    fun getMemberValidationResult(teamCreateState: TeamCreateState): ValidationResult {
        val teamModel =
            TeamModel(teamCreateState.name, teamCreateState.selectedMembers.map { it.id })
        return teamModel.validateMembers()
    }

    /**
     * Gets the validation results of the select members. This is being done to give specific
     * error results which could be handled in the presentation layer for input-specific error messages
     * @author Reshwan Barhoe
     * @param teamCreateState the state for creating a team
     * @return ValidationResult
     */
    fun getSelectedMemberValidationResult(teamCreateState: TeamCreateState): ValidationResult {
        val teamModel =
            TeamModel(teamCreateState.name, teamCreateState.selectedMembers.map { it.id })
        return teamModel.validateSelectedMembers()
    }

    fun getSavingSelectedMemberValidationResult(teamCreateState: TeamCreateState): ValidationResult {
        val teamModel =
            TeamModel(teamCreateState.name, teamCreateState.selectedMembers.map { it.id })
        return teamModel.validateMembers()
    }

    /**
     * Calls the repository to create a team with provided information
     * @author Reshwan Barhoe
     * @param teamCreateState the state for creating a team
     * @return A resource hierarchy class that contain info
     */
    suspend fun createTeam(teamCreateState: TeamCreateState): TeamResource<ResponseId> {
        val memberIds = teamCreateState.members.map { it.id }
        val teamModel = TeamModel(teamCreateState.name, memberIds)
        return teamRepository.saveTeam(teamModel)
    }
}