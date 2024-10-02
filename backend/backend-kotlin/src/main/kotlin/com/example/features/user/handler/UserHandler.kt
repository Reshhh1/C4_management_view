package com.example.features.user.handler

import com.example.features.session.dtos.*
import com.example.features.user.controller.*
import com.example.features.user.dtos.*
import com.example.general.exception.*
import com.example.util.enums.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class UserHandler(private val userController: UserController) {

    /**
     * handles the authentication for when updating user details
     * @author Ömer Aynaci
     * @param call the application call
     */
    suspend fun updateUserDetails(call: ApplicationCall) {
        val principal = call.principal<JWTPrincipal>()
        val updateUser = call.receive<UpdateUserDTO>()
        if (principal != null) {
            val email = principal["email"]
            if (email != null) {
                userController.updateUserDetails(email, updateUser, call)
                call.respond(HttpStatusCode.OK)
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, ResponseMessage(ErrorCode.G_UNAUTHORIZED.code))
        }
    }

    /**
     * Handles the authentication for when updating user password
     * @author Ömer Aynaci
     * @param call the application call
     */
    suspend fun updatePassword(call: ApplicationCall) {
        val principal = call.principal<JWTPrincipal>()
        val updatePassword = call.receive<UpdatePasswordDTO>()
        if (principal != null) {
            val email = principal["email"]
            if (email != null) {
                userController.updatePassword(email, updatePassword, call)
                call.respond(HttpStatusCode.OK)
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, ResponseMessage(ErrorCode.G_UNAUTHORIZED.code))
        }
    }

    /**
     * handles the authentication for displaying the first name of the user in the response
     * @author Ömer Aynaci
     * @param call the application call
     */
    suspend fun responseFirstName(call: ApplicationCall) {
        val principal = call.principal<JWTPrincipal>()
        if (principal != null) {
            val email = principal["email"]
            val firstName = userController.getUserFirstName(call, email!!)
            call.respond(HttpStatusCode.OK, DashboardMessage(firstName))
        } else {
            call.respond(HttpStatusCode.Unauthorized, ResponseMessage(ErrorCode.G_UNAUTHORIZED.code))
        }
    }

}