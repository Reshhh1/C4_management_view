package com.example.plugins

import com.example.features.session.authentication.JWTAuthentication
import com.example.features.user.util.di.components.DaggerUserHandlerComponent
import com.example.general.exception.ResponseMessage
import com.example.util.enums.ErrorCode
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*


/**
 * configures the JWT authorization
 * @author Reshwan Barhoe & Ömer Aynaci
 */
fun Application.configureSecurity() {
    install(Authentication) {
        jwt("auth-jwt") {
            val jwtAuthentication = JWTAuthentication(
                DaggerUserHandlerComponent.create().getUserRepository()
            )
            realm = "Access to hello"
            verifier(
                jwtAuthentication.verifyToken()
            )
            validate { credential ->
                jwtAuthentication.authorizeUser(credential)
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, ResponseMessage(ErrorCode.G_SESSION_EXPIRED.code))
            }
        }
    }
}

