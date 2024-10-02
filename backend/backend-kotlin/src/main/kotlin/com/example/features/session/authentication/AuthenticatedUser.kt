package com.example.features.session.authentication

import io.ktor.server.auth.*

data class AuthenticatedUser(
    val userId: Int,
    val firstName: String,
) : Principal
