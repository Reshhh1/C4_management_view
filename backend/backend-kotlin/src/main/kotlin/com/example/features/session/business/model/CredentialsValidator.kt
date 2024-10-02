package com.example.features.session.business.model

import kotlinx.serialization.*
import org.mindrot.jbcrypt.*


@Serializable
class CredentialsValidator(val email: String, val password: String) {

    @Contextual
    private val emailRegex: Regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")

    /**
     * validates the email
     * @author Ömer Aynaci
     * @return true if the email matches the email regex otherwise false
     */
    fun validateEmail(): Boolean {
        return email.matches(emailRegex)
    }

    /**
     * checks if the plain password with the hashed password are matching
     * @author Ömer Aynaci
     * @param hashedPassword the hashed password
     * @return true if the plain password matches the hashed password
     */
    fun doesPasswordMatch(hashedPassword: String): Boolean {
        return BCrypt.checkpw(password, hashedPassword)
    }
}