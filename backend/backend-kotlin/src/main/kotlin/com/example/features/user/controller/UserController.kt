package com.example.features.user.controller

import com.example.features.team.dtos.*
import com.example.features.user.business.service.*
import com.example.features.user.dtos.*
import com.example.general.exception.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import javax.inject.*

class UserController @Inject constructor(
    private val userService: UserService,
    private val exceptionHandler: ExceptionHandler
) {

    /**
     * Handles the HTTP request to retrieve the user details by the given id
     * @author Reshwan Barhoe
     * @param id The unique identifier of the user
     * @param call The ApplicationCall for the representing the HTTP request
     */
    suspend fun getUserDetailsById(id: Int, call: ApplicationCall) {
        try {
            val userDetails = userService.getUserDetailsById(id)
            call.respond(HttpStatusCode.OK, userDetails)
        } catch (exception: Exception) {
            exceptionHandler.handleExceptionResponse(exception, call)
        }
    }

    /**
     * Handles the HTTP request to create a new user
     * @author Ömer Aynaci
     * @param call the application call
     * @param createUserDTO the request body
     */
    suspend fun createUser(call: ApplicationCall, createUserDTO: CreateUserDTO) {
        try {
            userService.createUser(createUserDTO)
            call.respond(HttpStatusCode.Created)
        } catch (exception: Exception) {
            exceptionHandler.handleExceptionResponse(exception, call)
        }
    }

    /**
     * Handles the HTTP request to retrieves the search result by the given
     * search options
     * @author Reshwan Barhoe
     * @param searchOptions the provided search options
     * @param call The ApplicationCall representing the HTTP request
     */
    suspend fun getUsersBySearchOptions(searchOptions: SearchOptions, call: ApplicationCall) {
        try {
            val searchResult = userService.getUsersBySearchOptions(searchOptions)
            call.respond(HttpStatusCode.OK, searchResult)
        } catch (exception: Exception) {
            exceptionHandler.handleExceptionResponse(exception, call)
        }
    }

    /**
     * handles the HTTP request for updating user details
     * @author Ömer Aynaci
     * @param email the email of the user
     * @param updateUserModel the request body
     * @param call the application call
     */
    suspend fun updateUserDetails(email: String, updateUserModel: UpdateUserDTO, call: ApplicationCall) {
        try {
            userService.updateUserDetails(email, updateUserModel)
            call.respond(HttpStatusCode.OK)
        } catch (exception: Exception) {
            exceptionHandler.handleExceptionResponse(exception, call)
        }
    }


    /**
     * Handles the HTTP request for updating the password
     * @author Ömer Aynaci
     * @param email the email of the user
     * @param updatePasswordDTO the request body
     * @param call the application call
     */
    suspend fun updatePassword(email: String, updatePasswordDTO: UpdatePasswordDTO, call: ApplicationCall) {
        try {
            userService.updatePassword(email, updatePasswordDTO)
            call.respond(HttpStatusCode.OK)
        } catch (exception: Exception) {
            exceptionHandler.handleExceptionResponse(exception, call)
        }
    }

    /**
     * gets the first name of the user when something goes wrong it returns an empty string
     * @author Ömer Aynaci
     * @param call the application call
     * @param email the email of the user
     * @return the email
     */
    suspend fun getUserFirstName(call: ApplicationCall, email: String): String {
        try {
            return userService.getFirstNameByEmail(email)
        } catch (exception: Exception) {
            exceptionHandler.handleExceptionResponse(exception, call)
        }
        return email
    }
}