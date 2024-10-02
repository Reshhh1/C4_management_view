package com.example.features.session.requestValidation

import com.example.features.session.authentication.*
import com.example.features.session.business.model.*
import com.example.util.enums.*
import io.ktor.server.plugins.*

class UserAuthenticatorValidator {

    /**
     * checks if the password is empty
     * @author Ömer Aynaci
     * @param userAuthenticator the login body
     * @return true if password is not empty otherwise respond to error
     */
    private fun isPasswordEmpty(
        userAuthenticator: UserAuthenticator
    ) {
        val password = userAuthenticator.password
        if (password.isEmpty()) {
           throw BadRequestException(ErrorCode.U_INVALID_CREDENTIALS.code)
        }
    }

    /**
     * checks if the email is empty and if an email is a valid email
     * @author Ömer Aynaci
     * @param userAuthenticator the login body
     * @return true if email is not empty and if an email is valid email is not empty otherwise respond to error
     */
    private fun validateAndCheckEmail(
        userAuthenticator: UserAuthenticator
    ) {
        val email = userAuthenticator.email
        val credentialsValidator =
            CredentialsValidator(email, userAuthenticator.password)
        if (email.isEmpty() || !credentialsValidator.validateEmail()) {
            throw BadRequestException(ErrorCode.U_INVALID_CREDENTIALS.code)
        }
    }


    /**
     * validates the email and password
     * @author Ömer Aynaci
     * @param userAuthenticator the login body
     */
    fun validateCredentials(userAuthenticator: UserAuthenticator) {
        validateAndCheckEmail(userAuthenticator)
        isPasswordEmpty(userAuthenticator)
    }
}