package com.example.frontend_android.teams.data.repository

import com.example.frontend_android.teams.data.remote.dtos.response.TeamSummary
import com.example.frontend_android.teams.domain.errors.TeamResource
import com.example.frontend_android.teams.domain.model.TeamModel
import com.example.frontend_android.util.Resource
import com.example.frontend_android.util.ResponseId

/**
 * Interface of the team repository
 * @author Reshwan Barhoe
 */
interface TeamRepository {
    suspend fun getTeamSummaries(): Resource<List<TeamSummary>>
    suspend fun saveTeam(requestBody: TeamModel): TeamResource<ResponseId>
}