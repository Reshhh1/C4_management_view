package com.example.plugins

import com.example.general.exception.*
import com.example.util.enums.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.*

/**
 * Handles the global exceptions to the application
 * @author Reshwan Barhoe
 */
fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<NumberFormatException> { call, _ ->
            call.respond(HttpStatusCode.UnprocessableEntity, ResponseMessage(ErrorCode.G_NUMERIC_ID_REQUIRED.code))
        }

        exception<SerializationException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest, ResponseMessage("WRONG"))
        }
        exception<BadRequestException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest, ResponseMessage(ErrorCode.G_INVALID_REQUEST_BODY.code))
        }
    }
}