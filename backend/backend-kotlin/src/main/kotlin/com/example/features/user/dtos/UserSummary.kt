package com.example.features.user.dtos

import kotlinx.serialization.Serializable

/**
 * DTO that's being used for displaying
 * a list of users
 * @author Reshwan Barhoe
 */
@Serializable
data class UserSummary(
    val id: Int,
    val firstName: String,
    val prefixes: String,
    val lastName: String
)