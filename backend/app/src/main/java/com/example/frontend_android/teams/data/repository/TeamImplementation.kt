package com.example.frontend_android.teams.data.repository

import com.example.frontend_android.teams.data.remote.TeamApi
import com.example.frontend_android.teams.data.remote.dtos.response.TeamSummary
import com.example.frontend_android.teams.domain.errors.TeamResource
import com.example.frontend_android.teams.domain.model.TeamModel
import com.example.frontend_android.util.Constants
import com.example.frontend_android.util.Resource
import com.example.frontend_android.util.ResponseId

class TeamImplementation(
    private val teamApi: TeamApi
) : TeamRepository {

    /**
     * Fetches the team summary
     * @author Reshwan Barhoe
     * @return A resource hierarchy class that contain info
     */
    override suspend fun getTeamSummaries(): Resource<List<TeamSummary>> {
        return try {
            Resource.Success(teamApi.getTeamSummaries())
        } catch (exception: Exception) {
            Resource.Error("Oops, something went wrong. Try again later")
        }
    }

    /**
     * Sends a request to create a team
     * The response error codes / network requests are being
     * handled
     * @author Reshwan Barhoe
     * @param requestBody TeamModel that's being saved
     * @return A resource hierarchy of the team.
     *
     */
    override suspend fun saveTeam(requestBody: TeamModel): TeamResource<ResponseId> {
        return try {
            val response = teamApi.createTeam(requestBody)
            if (response.isSuccessful) {
                TeamResource.Success(response.body())
            } else {
                handleCreateTeamStatusCodes(response.code())
            }
        } catch (exception: Exception) {
            TeamResource.NetworkError(Constants.NETWORK_ERROR)
        }
    }

    /**
     * Handles the status codes for creating a team
     * @author Reshwan Barhoe
     * @param statusCode that's being handled
     * @return A resource hierarchy of the team.
     */
    private fun handleCreateTeamStatusCodes(statusCode: Int): TeamResource<ResponseId> {
        return when (statusCode) {
            409 -> TeamResource.NameAlreadyExists("Team name already exists")
            else -> TeamResource.NetworkError("ErrorCode not implemented")
        }
    }
}