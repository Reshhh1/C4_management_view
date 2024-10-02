package com.example.team.helpers

import com.example.features.team.data.entity.TeamEntity
import com.example.features.user.data.entity.UserEntity
import com.example.team.helpers.model.LinkedTeam
import com.example.team.helpers.model.TestTeam
import com.example.user.helpers.UserTestHelper
import com.example.util.enums.ErrorCode
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.SizedCollection
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TeamTestHelper {
    val createdAtTime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    /**
     * Provides the test data for get teams tests
     * @author Reshwan Barhoe
     */
    fun provideTeamGETData() {
        listOfGetMembers.map { UserTestHelper.insertUser(it) }
        linkedGETTeams.map { insertAndLinkTeams(it) }
    }

    /**
     * Provides the test data for post teams tests
     * @author Reshwan Barhoe
     */
    fun provideTeamPOSTData() {
        listOfPostMembers.map { UserTestHelper.insertUser(it) }
        linkedPOSTTeams.map { insertAndLinkTeams(it) }
    }

    /**
     * Inserts and links a team with users
     * @author Reshwan Barhoe
     * @param linkedTeam that's being used for inserting and linking teams
     */
    private fun insertAndLinkTeams(linkedTeam: LinkedTeam) {
        val memberEntities = getUserEntities(linkedTeam.members)
        insertAndReturnTeamEntity(linkedTeam.testTeam).members = SizedCollection(memberEntities)
    }

    /**
     * Creates an insert team statement
     * @author Reshwan Barhoe
     * @param teamModel with the required information
     * @return a team entity
     */
    private fun insertAndReturnTeamEntity(teamModel: TestTeam): TeamEntity {
        return TeamEntity.new {
            name = teamModel.name
            createdBy = teamModel.createdBy
        }
    }

    /**
     * Gets the user entities, from the list of ids
     * @author Reshwan Barhoe
     * @param listOfIds that's being used to get their user entity
     * @return a list of user entities
     */
    private fun getUserEntities(listOfIds: List<Int>): List<UserEntity> {
        return listOfIds.map { userId ->
           UserEntity.findById(userId)
                ?: throw NotFoundException(ErrorCode.T_MEMBER_NOT_FOUND.code)
        }
    }
}