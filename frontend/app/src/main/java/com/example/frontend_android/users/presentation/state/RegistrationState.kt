package com.example.frontend_android.users.presentation.state

/**
 * state for the registration
 * @author Ömer Aynaci
 * @property firstName the first name
 * @property lastName the last name
 * @property prefixes the prefixes
 * @property email the email
 * @property password the password
 */
data class RegistrationState(
    var firstName: String = "",
    val lastName: String = "",
    val prefixes: String? = "",
    val email: String = "",
    val password: String = ""
)