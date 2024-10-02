package com.example.features.team.controller

import com.example.features.team.business.service.TeamService
import com.example.features.team.dtos.CreateTeamDTO
import com.example.general.dtos.ResponseIdDto
import com.example.general.exception.ExceptionHandler
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import javax.inject.Inject

class TeamController @Inject constructor(
    private val teamService: TeamService,
    private val exceptionHandler: ExceptionHandler
) {

    /**
     * Handles the HTTP request for creating a team
     *
     * @author Reshwan Barhoe
     * @param createTeamDTO the expected request body
     * @param call The ApplicationCall representing the HTTP request
     */
    suspend fun createTeamAndReturnId(createTeamDTO: CreateTeamDTO, call: ApplicationCall) {
        try {
            val teamId = teamService.createTeamAndReturnId(createTeamDTO, 1)
            call.respond(HttpStatusCode.Created, ResponseIdDto(teamId))
        } catch(exception: Exception) {
            exceptionHandler.handleExceptionResponse(exception, call)
        }
    }

    /**
     * Handles the HTTP request for getting a list of teams
     *
     * @author Reshwan Barhoe
     * @param call The ApplicationCall representing the HTTP request
     */
    suspend fun getTeamsList(call: ApplicationCall) {
        try {
            val teamList = teamService.getTeamSummaries()
            call.respond(HttpStatusCode.OK, teamList)
        } catch (exception: Exception) {
            exceptionHandler.handleExceptionResponse(exception, call)
        }
    }

    /**
     * Handles the HTTP request for getting the details of a specific team
     *
     * @author Reshwan Barhoe
     * @param id that's being searched for
     * @param call The ApplicationCall representing the HTTP request
     */
    suspend fun getTeamDetailsById(id: Int, call: ApplicationCall) {
        try {
            val teamDetails = teamService.getTeamDetailsById(id)
            call.respond(HttpStatusCode.OK, teamDetails)
        } catch (exception: Exception) {
            exceptionHandler.handleExceptionResponse(exception, call)
        }
    }

}