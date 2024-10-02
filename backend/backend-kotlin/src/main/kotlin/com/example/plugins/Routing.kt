package com.example.plugins

import com.example.features.session.routing.*
import com.example.features.session.util.di.components.*
import com.example.features.team.routing.*
import com.example.features.team.util.di.components.*
import com.example.features.user.routing.*
import com.example.features.user.util.di.components.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

/**
 * Configuration of the application routing
 * @author Reshwan Barhoe
 */
fun Application.configureRouting() {
    val sessionController = DaggerSessionHandlerComponent
        .create()
        .getSessionController()
    val userController = DaggerUserHandlerComponent
        .create()
        .getUserController()
    val teamController = DaggerTeamHandlerComponent
        .create()
        .getTeamController()
    routing {
        userRouting(userController)
        sessionRoutes(sessionController)
        teamRoutes(teamController)
    }
}
