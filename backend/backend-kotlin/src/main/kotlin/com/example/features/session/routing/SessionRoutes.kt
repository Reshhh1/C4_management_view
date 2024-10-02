package com.example.features.session.routing

import com.example.features.session.controller.*
import com.example.features.session.handler.*
import io.ktor.server.application.*
import io.ktor.server.routing.*


/**
 * the routes for sessions are here defined
 *
 * @param sessionController the session controller
 * @author Ömer Aynaci
 */
fun Route.sessionRoutes(sessionController: SessionController) {
    route("/sessions") {
        post {
            SessionHandler(sessionController).loginUser(call)
        }
    }
}