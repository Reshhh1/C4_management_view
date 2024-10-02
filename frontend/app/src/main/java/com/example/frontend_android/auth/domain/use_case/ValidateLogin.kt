package com.example.frontend_android.auth.domain.use_case

import android.util.Patterns

class ValidateLogin {

    /**
     * Validation for the email
     * @author Reshwan Barhoe
     * @param email that's being validated
     * @return a validation result
     */
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(
                successful = false,
                errorMessage = "The email can't be blank"
            )

            !matchesEmailPattern(email) -> ValidationResult(
                successful = false,
                errorMessage = "Please enter a valid email"
            )

            else -> ValidationResult(successful = true)
        }
    }

    /**
     * Validation for the password
     * @author Reshwan Barhoe
     * @param password that's being validated
     * @return a validation result
     */
    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult(
                successful = false,
                errorMessage = "The password can't be blank"
            )

            else -> ValidationResult(successful = true)
        }
    }

    /**
     * Checks if the provided email matches to the built-in
     * email pattern
     * @author Reshwan Barhoe
     * @param email email that's being checked
     * @return if the email is valid or not
     */
    private fun matchesEmailPattern(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}