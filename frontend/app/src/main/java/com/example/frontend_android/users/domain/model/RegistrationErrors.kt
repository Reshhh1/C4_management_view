package com.example.frontend_android.users.domain.model

import com.example.frontend_android.auth.domain.use_case.ValidationResult

/**
 * data class for using the errors of the input fields if an error occurs
 * @author Ömer Aynaci
 * @property firstNameError the error of first name
 * @property prefixesError the error of the prefixes
 * @property lastNameError the error of the last name
 * @property emailError the error of email
 * @property passwordError the error of password
 */
data class RegistrationErrors(
    val firstNameError: ValidationResult,
    val prefixesError: ValidationResult,
    val lastNameError: ValidationResult,
    val emailError: ValidationResult,
    val passwordError: ValidationResult
)
