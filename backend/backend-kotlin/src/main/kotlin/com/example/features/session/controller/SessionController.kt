package com.example.features.session.controller

import com.example.features.session.authentication.*
import com.example.features.session.business.service.*
import com.example.general.exception.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import javax.inject.*

class SessionController @Inject constructor(
    private val sessionService: SessionService,
    private val exceptionHandler: ExceptionHandler
    ) {

    /**
     * Logs a user in if user exists.
     * if so log the user in then add a token to the cookies
     * @author Ömer Aynaci
     * @param call the application call
     */
    suspend fun login(call: ApplicationCall, userAuthenticator: UserAuthenticator) {
        try {
            val token = sessionService.login(userAuthenticator)
            val cookie = Cookie(name = "token", value = token)
            call.response.cookies.append(cookie)
            call.response.call.respond(HttpStatusCode.OK)

        } catch (exception: Exception) {
            exceptionHandler.handleExceptionResponse(exception, call)
        }
    }
}