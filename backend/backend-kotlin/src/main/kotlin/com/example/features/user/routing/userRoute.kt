package com.example.features.user.routing

import com.example.features.team.dtos.*
import com.example.features.user.controller.*
import com.example.features.user.dtos.*
import com.example.features.user.handler.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

/**
 * User routing
 * @author Reshwan Barhoe
 */
fun Route.userRouting(userController: UserController) {
    route("users") {
        get("{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw NumberFormatException()
            userController.getUserDetailsById(id, call)
        }

        post {
            val userBusiness = call.receive<CreateUserDTO>()
            userController.createUser(call, userBusiness)
        }

        authenticate("auth-jwt") {
            get("/firstname") {
                UserHandler(userController).responseFirstName(call)
            }
            put {
                UserHandler(userController).updateUserDetails(call)
            }

            put("/password") {
                UserHandler(userController).updatePassword(call)
            }
        }

        get {
            val searchOptions = SearchOptions(
                searchTerm = call.request.queryParameters["name"]
            )
            userController.getUsersBySearchOptions(searchOptions, call)
        }
    }
}