package com.example.frontend_android.users.data.remote.dtos.request

import kotlinx.serialization.Serializable

/**
 * request body for registration
 * @author Ömer Aynaci
 * @property firstName the first name
 * @property lastName the last name
 * @property prefixes the prefixes
 * @property email the email
 * @property password the password
 */
@Serializable
data class RegistrationRequest(
    val firstName: String,
    val lastName: String,
    val prefixes: String?,
    val email: String,
    val password: String
)