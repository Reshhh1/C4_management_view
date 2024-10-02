package com.example.util.converters

import com.example.features.team.business.model.TeamModel
import com.example.features.team.data.model.Team
import com.example.features.team.data.model.Team.createdAt
import com.example.features.team.data.model.Team.createdBy
import com.example.features.team.data.model.Team.name
import com.example.features.team.dtos.BasicTeamData
import com.example.features.team.dtos.CreateTeamDTO
import com.example.features.team.dtos.TeamDetails
import com.example.features.team.dtos.TeamSummary
import com.example.features.user.dtos.UserSummary
import org.jetbrains.exposed.sql.ResultRow
import java.time.format.DateTimeFormatter

class TeamFactory: Converter<TeamModel> {
    override fun convertToBusinessModel(row: ResultRow): TeamModel {
        TODO("gets implemented for the GET request issue")
    }

    /**
     * Converts the CreateTeamDTO to a TeamModel
     * @author Reshwan Barhoe
     * @param createTeamDTO dto that's being converted
     * @param createdBy property that's being used for the conversion
     * @return TeamModel
     */
    fun convertToBusinessModel(createTeamDTO: CreateTeamDTO, createdBy: Int): TeamModel {
        return TeamModel(
            name = createTeamDTO.name,
            members = createTeamDTO.members,
            createdBy = createdBy
        )
    }

    /**
     * Converts the CreateTeamDTO to a TeamModel
     * @author Reshwan Barhoe
     * @param row result row of the query
     * @return A summary of the team
     */
    fun convertToTeamSummary(row: ResultRow): TeamSummary {
        return TeamSummary(
            id = row[Team.id].value,
            name = row[name]
        )
    }

    /**
     * Converts the result row to a TeamData object
     * @author Reshwan Barhoe
     * @param row result row of the query
     * @return A TeamData object
     */
    fun convertToTeamData(row: ResultRow): BasicTeamData {
        return BasicTeamData(
            name = row[name],
            createdAt = row[createdAt],
            createdBy = row[createdBy]
        )
    }

    /**
     * Converts the provided information into a team detail object.
     * It also formats the date to the correct pattern
     * @author Reshwan Barhoe
     * @param teamData that's being used for the conversion
     * @param members that's being used for the conversion
     * @return a team details object
     */
    fun convertToTeamDetails(teamData: BasicTeamData, members: List<UserSummary>): TeamDetails {
        return TeamDetails(
            name = teamData.name,
            members = members,
            createdAt = teamData.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )
    }
}