package com.example.features.team.routing

import com.example.features.team.controller.TeamController
import com.example.features.team.dtos.CreateTeamDTO
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

/**
 * Accessible team routes
 * @author Reshwan Barhoe
 * @param teamController being used to start the proces
 */
fun Route.teamRoutes(teamController: TeamController) {
    route("teams") {
        post {
            val createTeamDTO = call.receive<CreateTeamDTO>()
            teamController.createTeamAndReturnId(createTeamDTO, call)
        }

        get {
            teamController.getTeamsList(call)
        }

        get("{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw NumberFormatException()
            teamController.getTeamDetailsById(id, call)
        }

    }
}