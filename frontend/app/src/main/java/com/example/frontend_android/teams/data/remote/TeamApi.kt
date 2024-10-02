package com.example.frontend_android.teams.data.remote

import com.example.frontend_android.teams.data.remote.dtos.response.TeamSummary
import com.example.frontend_android.teams.domain.model.TeamModel
import com.example.frontend_android.util.ResponseId
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * API interface for the team
 * @author Reshwan Barhoe
 */
interface TeamApi {
    @GET("teams")
    suspend fun getTeamSummaries(): List<TeamSummary>

    @POST("teams")
    suspend fun createTeam(@Body requestBody: TeamModel): Response<ResponseId>
}