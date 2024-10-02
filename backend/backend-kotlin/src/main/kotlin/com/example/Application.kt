package com.example

import com.example.plugins.*
import com.example.util.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}


fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureStatusPages()
    configureSecurity()
    configureRouting()
}