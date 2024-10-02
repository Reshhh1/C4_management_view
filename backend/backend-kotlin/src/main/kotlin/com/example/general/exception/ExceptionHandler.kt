package com.example.general.exception


import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import kotlinx.serialization.*

@Serializable
data class ResponseMessage(val errorCode: String)

class ExceptionHandler {
    /**
     * Handles the exception responses for the client
     * @author Reshwan Barhoe
     * @param exception Exception that's being thrown
     * @param call Application call that's being used for the response
     */
    suspend fun handleExceptionResponse(exception: Exception, call: ApplicationCall) {
        val responseMessage = ResponseMessage(exception.message.toString())

        when (exception) {
            is NotFoundException -> call.respond(HttpStatusCode.NotFound, responseMessage)
            is UnauthorizedError -> call.respond(HttpStatusCode.Unauthorized, responseMessage)
            is EntityExistsException -> call.respond(HttpStatusCode.Conflict, responseMessage)
            is BadRequestException -> call.respond(HttpStatusCode.BadRequest, responseMessage)
        }
    }
}