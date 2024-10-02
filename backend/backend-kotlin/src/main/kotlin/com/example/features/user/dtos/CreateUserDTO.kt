package com.example.features.user.dtos

import kotlinx.serialization.*

@Serializable
data class CreateUserDTO(
    val firstName: String,
    val lastName: String,
    val prefixes: String?,
    val email: String,
    val password: String
)