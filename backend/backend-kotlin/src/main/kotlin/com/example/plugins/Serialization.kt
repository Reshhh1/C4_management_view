package com.example.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

/**
 * Configures the serialization of the application
 * @author Reshwan Barhoe
 */
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
