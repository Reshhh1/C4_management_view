package com.example.features.team.business.service

import com.example.features.team.data.repository.TeamRepository
import com.example.features.team.dtos.CreateTeamDTO
import com.example.features.team.dtos.TeamDetails
import com.example.features.team.dtos.TeamSummary
import com.example.features.user.data.entity.UserEntity
import com.example.features.user.data.repository.UserRepository
import com.example.general.exception.EntityExistsException
import com.example.util.converters.TeamFactory
import com.example.util.enums.ErrorCode
import io.ktor.server.plugins.*
import javax.inject.Inject

class TeamService @Inject constructor(
    private val userRepository: UserRepository,
    private val teamRepository: TeamRepository,
    private val teamFactory: TeamFactory
) {

    /**
     * Creates a team based on the provided dto and id of the user that's creating
     * the team.
     *
     * @author Reshwan Barhoe
     * @param createTeamDTO object that contains information about the team to create
     * @param createdBy id of the user that's creating the team
     */
    suspend fun createTeamAndReturnId(createTeamDTO: CreateTeamDTO, createdBy: Int): Int {
        val teamModel = teamFactory.convertToBusinessModel(createTeamDTO, createdBy)
        teamModel.validateModel()
        validateTeamNameUniqueness(teamModel.name)
        val members = getMemberEntities(teamModel.members)
        return teamRepository.createTeamAndReturnId(teamModel,members)
    }

    /**
     * Gets a list of TeamSummary models
     * @author Reshwan Barhoe
     * @return a list of TeamSummary
     */
    suspend fun getTeamSummaries(): List<TeamSummary> {
        return teamRepository.getTeamsList()
    }

    /**
     * Gets the team details by their provided id
     * @author Reshwan Barhoe
     * @param id that's being searched for
     * @throws NotFoundException if the team details aren't found
     * @return a TeamDetails Object
     */
    suspend fun getTeamDetailsById(id: Int): TeamDetails {
        return teamRepository.getTeamDetailsById(id) ?: throw NotFoundException(ErrorCode.T_NOT_FOUND.code)
    }

    /**
     * Gets the member entities based on the provided userids. The member entities are used in the
     * data layer for linking a many-to-many table between the team and user.
     *
     * @author Reshwan Barhoe
     * @param userIds A list of user ids
     * @return a list of UserEntities
     */
    private suspend fun getMemberEntities(userIds: List<Int>): List<UserEntity> {
        val users = mutableListOf<UserEntity>()
        userIds.forEach { userId ->
            val user = userRepository.getUserEntityById(userId)
                ?: throw NotFoundException(ErrorCode.T_MEMBER_NOT_FOUND.code)
            users.add(user)
        }
        return users.toList()
    }

    /**
     * Checks if the provided name exists and handles based on that result
     *
     * @author Reshwan Barhoe
     * @param teamName is the name that's being checked
     * @throws EntityExistsException if the provided name already exists
     */
    private suspend fun validateTeamNameUniqueness(teamName: String) {
        if(teamRepository.doesTeamExists(teamName)) {
            throw EntityExistsException(ErrorCode.T_NAME_EXISTS.code)
        }
    }
}