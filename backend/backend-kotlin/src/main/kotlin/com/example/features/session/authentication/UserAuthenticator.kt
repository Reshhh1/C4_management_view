package com.example.features.session.authentication


import com.example.features.session.business.model.*
import com.example.general.exception.*
import com.example.util.enums.*
import kotlinx.serialization.*

@Serializable
data class UserAuthenticator(val email: String, val password: String) {


    @Serializable
    private val credentialsValidator = CredentialsValidator(email, password)

    /**
     * matches the plain password with the hashed password
     *
     * @param hashedPassword the hashed password that is stored
     * @return true if the password matches otherwise false
     * @author Ömer Aynaci
     */
    fun doesPasswordMatch(hashedPassword: String): Boolean {
        return credentialsValidator.doesPasswordMatch(hashedPassword)
    }

    /**
     * validating password
     * @param password the password of the user
     * @throws UnauthorizedError if password is not valid it throws an unauthorized error
     * @author Ömer Aynaci
     */
    fun validatePasswordForAuthentication(password: String) {
        if (!credentialsValidator.doesPasswordMatch(password)) {
            throw UnauthorizedError(ErrorCode.G_UNAUTHORIZED.code)
        }
    }
}