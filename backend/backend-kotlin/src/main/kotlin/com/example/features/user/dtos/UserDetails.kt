package com.example.features.user.dtos

import kotlinx.serialization.Serializable

/**
 * User details DTO
 * @author Reshwan Barhoe
 */
@Serializable
data class UserDetails(
    val firstName: String,
    val prefixes: String?,
    val lastName: String,
    val email: String
)
