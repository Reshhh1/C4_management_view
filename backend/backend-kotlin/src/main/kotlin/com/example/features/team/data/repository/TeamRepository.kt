package com.example.features.team.data.repository

import com.example.features.team.business.model.TeamModel
import com.example.features.team.data.entity.TeamEntity
import com.example.features.team.data.model.Team
import com.example.features.team.dtos.BasicTeamData
import com.example.features.team.dtos.TeamDetails
import com.example.features.team.dtos.TeamSummary
import com.example.features.user.data.entity.UserEntity
import com.example.features.user.dtos.UserSummary
import com.example.util.DatabaseFactory.dbQuery
import com.example.util.converters.TeamFactory
import com.example.util.converters.UserFactory
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import javax.inject.Inject

class TeamRepository @Inject constructor(
    private val teamFactory: TeamFactory,
    private val userFactory: UserFactory
) {
    /**
     * Creates a new team with the provided information,
     * sets the list of members and returns the team's id
     * @author Reshwan Barhoe
     * @param teamModel the model that contains information about the team to create
     * @param members the list of members that's being added to the team
     * @return the newly created team's id
     */
    suspend fun createTeamAndReturnId(teamModel: TeamModel, members: List<UserEntity>): Int = dbQuery {
        val teamEntity = TeamEntity.new {
            name = teamModel.name
            createdBy = teamModel.createdBy
        }
        teamEntity.members = SizedCollection(members)
        return@dbQuery teamEntity.id.value
    }

    /**
     * Returns if entity rows are found in the database. This
     * is being used to check if a team with the provided name already exists
     * @author Reshwan Barhoe
     * @param teamName that's being checked
     * @return [Boolean] based on the amount of rows
     */
    suspend fun doesTeamExists(teamName: String) = dbQuery {
        return@dbQuery Team.select { Team.name eq teamName }.count() > 0
    }

    /**
     * Returns a list of TeamSummary.
     * There is a maximum of <maxTeamAmount> entities that are being selected
     * The list is ordered by the team's name ascending
     * Each entity is being converted into an TeamSummary
     * @author Reshwan Barhoe
     * @return a list of TeamSummary's
     */
    suspend fun getTeamsList(): List<TeamSummary> = dbQuery {
        val maxTeamAmount = 100
        return@dbQuery Team.selectAll()
            .orderBy(
                column = Team.name,
                order = SortOrder.ASC
            )
            .limit(maxTeamAmount)
            .map { teamFactory.convertToTeamSummary(it) }
    }

    /**
     * Gets the team members by their provided id,
     * the result is getting converted into a UserSummary and
     * sorted by firstname
     * @author Reshwan Barhoe
     * @param id that's being searched for
     * @return a list of UserSummaries or null
     */
    private suspend fun getTeamMemberSummariesById(id: Int):  List<UserSummary>? = dbQuery {
        return@dbQuery TeamEntity.findById(id).let {
            it?.members
        }
            ?.sortedBy { it.firstName }
            ?.map {
                userFactory.convertToUserSummary(it)
            }
    }

    /**
     * Gets the basic team data by the provided id. The result
     * is being converted in a TeamData object.
     * Only 1 object is being returned or else the result will be null
     * @author Reshwan Barhoe
     * @param id that's being searched for
     * @return a single BasicTeamData object or null
     */
    private suspend fun getBasicTeamDataById(id: Int): BasicTeamData? = dbQuery {
        return@dbQuery Team.select { Team.id eq id }
            .map { teamFactory.convertToTeamData(it) }
            .singleOrNull()
    }

    /**
     * Gets the team details by the provided id
     * @author Reshwan Barhoe
     * @param id that's being searched for
     * @return a TeamDetails object or null
     */
    suspend fun getTeamDetailsById(id: Int): TeamDetails? = dbQuery {
        val teamData = getBasicTeamDataById(id) ?: return@dbQuery null
        val members = getTeamMemberSummariesById(id) ?: return@dbQuery null
        return@dbQuery teamFactory.convertToTeamDetails(teamData, members)
    }
}