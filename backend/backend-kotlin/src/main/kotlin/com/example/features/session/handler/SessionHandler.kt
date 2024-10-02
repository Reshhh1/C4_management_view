package com.example.features.session.handler

import com.example.features.session.authentication.*
import com.example.features.session.controller.*
import com.example.features.session.requestValidation.*
import io.ktor.server.application.*
import io.ktor.server.request.*

class SessionHandler(private val sessionController: SessionController) {

    /**
     * Handles the login
     * @author Ömer Aynaci
     * @param call the application call
     */
    suspend fun loginUser(call: ApplicationCall) {
        val userAuthenticator = call.receive<UserAuthenticator>()
        val userValidator = UserAuthenticatorValidator()
        userValidator.validateCredentials(userAuthenticator)
        sessionController.login(call, userAuthenticator)
    }
}