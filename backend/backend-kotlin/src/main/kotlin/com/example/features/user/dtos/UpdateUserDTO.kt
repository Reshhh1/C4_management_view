package com.example.features.user.dtos

import com.example.util.enums.*
import kotlinx.serialization.*

@Serializable
data class UpdateUserDTO(
    val firstName: String,
    val lastName: String,
    val prefixes: String?,
    val email: String,
    val role: Roles
)