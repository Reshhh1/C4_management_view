package com.example.frontend_android.users.domain

import android.util.Patterns
import com.example.frontend_android.auth.domain.use_case.ValidationResult

class RegistrationValidator {

    /**
     * validating the first name
     * @author Ömer Aynaci
     * @param firstName the first name
     * @return an instance of ValidationResult
     */
    fun validateFirstName(firstName: String): ValidationResult {
        return when {
            firstName.length < 2 -> ValidationResult(
                successful = false,
                errorMessage = "First name must be at least 2 characters long"
            )

            firstName.length > 255 -> ValidationResult(
                successful = false,
                errorMessage = "First name cannot be longer than 255 characters"
            )

            else -> ValidationResult(successful = true)
        }
    }

    /**
     * validating the prefixes
     * @author Ömer Aynaci
     * @param prefixes the prefixes
     * @return an instance of ValidationResult
     */
    fun validatePrefixes(prefixes: String?): ValidationResult {
        return when {
            prefixes!!.length > 20 -> ValidationResult(
                successful = false,
                errorMessage = "Prefixes cannot exceed 20 characters"
            )

            else -> ValidationResult(successful = true)
        }
    }

    /**
     * validating the last name
     * @author Ömer Aynaci
     * @param lastName the last name
     * @return an instance of ValidationResult
     */
    fun validateLastName(lastName: String): ValidationResult {
        return when {
            lastName.length < 2 -> ValidationResult(
                successful = false,
                errorMessage = "Last name must be at least 2 characters long"
            )

            lastName.length > 255 -> ValidationResult(
                successful = false,
                errorMessage = "Last name cannot be longer than 255 characters"
            )

            else -> ValidationResult(successful = true)
        }
    }

    /**
     * validating the email
     * @author Ömer Aynaci
     * @param email the email
     * @return an instance of ValidationResult
     */
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(
                successful = false,
                errorMessage = "The email can't be blank"
            )

            !isEmailValid(email) -> ValidationResult(
                successful = false,
                errorMessage = "Please enter a valid email address"
            )

            else -> ValidationResult(successful = true)
        }
    }

    /**
     * validates the password for the requirements
     * @author Ömer Aynaci
     * @param password the password
     * @return a object of ValidationResult
     */
    fun validatePassword(password: String): ValidationResult {
        return when {
            !isPasswordLengthValid(password) -> getPasswordLengthError()

            !doesPasswordContainLowercase(password) -> getPasswordLowercaseError()

            !doesPasswordContainUppercase(password) -> getPasswordUppercaseError()

            !doesPasswordContainNumbers(password) -> getPasswordDigitError()

            !doesPasswordContainSpecialCharacters(password) -> getPasswordSpecialCharactersError()

            else -> ValidationResult(successful = true)
        }
    }

    /**
     * gets the password error for password length
     * @author Ömer Aynaci
     * @return object of ValidationResult
     */
    private fun getPasswordLengthError(): ValidationResult {
        return ValidationResult(
            successful = false,
            errorMessage = "Password should have at least 8 characters"
        )
    }

    /**
     * gets the password error for password lowercase requirement
     * @author Ömer Aynaci
     * @return object of ValidationResult
     */
    private fun getPasswordLowercaseError(): ValidationResult {
        return ValidationResult(
            successful = false,
            errorMessage = "Password should contain at least 1 lowercase"
        )
    }

    /**
     * gets the password error for password uppercase requirement
     * @author Ömer Aynaci
     * @return object of ValidationResult
     */
    private fun getPasswordUppercaseError(): ValidationResult {
        return ValidationResult(
            successful = false,
            errorMessage = "Password should contain at least 1 uppercase"
        )
    }

    /**
     * gets the password error for password special character requirement
     * @author Ömer Aynaci
     * @return object of ValidationResult
     */
    private fun getPasswordSpecialCharactersError(): ValidationResult {
        return ValidationResult(
            successful = false,
            errorMessage = "Password should contain at least 1 special character"
        )
    }

    /**
     * gets the password error for password digit requirement
     * @author Ömer Aynaci
     * @return object of ValidationResult
     */
    private fun getPasswordDigitError(): ValidationResult {
        return ValidationResult(
            successful = false,
            errorMessage = "Password should contain at least 1 number"
        )
    }

    /**
     * validates the email address
     * @author Ömer Aynaci
     * @param email the email
     * @return true if email address matches the pattern
     */
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * checks if the password contains uppercase letters
     * @author Ömer Aynaci
     * @param password the password
     * @return true if the password contains uppercase letters otherwise false
     */
    private fun doesPasswordContainUppercase(password: String): Boolean {
        val uppercaseRegex = Regex("^(?=.*[A-Z]).+\$")
        return password.contains(uppercaseRegex)
    }

    /**
     *
     * checks if the password contains lower case letters
     * @param password the password
     * @return true if the password contains lower case letters otherwise false
     */
    private fun doesPasswordContainLowercase(password: String): Boolean {
        val lowercaseRegex = Regex("^(?=.*[a-z]).+\$")
        return password.contains(lowercaseRegex)
    }

    /**
     * checks if the password contains special characters
     * @author Ömer Aynaci
     * @param password the password
     * @return true if the password contains special character otherwise false
     */
    private fun doesPasswordContainSpecialCharacters(password: String): Boolean {
        val specialCharacterRegex = Regex("^(?=.*[\\W_]).+\$")
        return password.contains(specialCharacterRegex)
    }

    /**
     *
     * checks if the password contains a number
     * @param password the password
     * @return true if the password contains a number
     */
    private fun doesPasswordContainNumbers(password: String): Boolean {
        val numberRegex = Regex("^(?=.*\\d).+\$")
        return password.contains(numberRegex)
    }

    /**
     * checks if the password length is greater than 8 characters
     * @author Ömer Aynaci
     * @param password the password
     * @return true if the password length is greater than 8 characters otherwise false
     */
    private fun isPasswordLengthValid(password: String): Boolean {
        return password.length > 8
    }
}