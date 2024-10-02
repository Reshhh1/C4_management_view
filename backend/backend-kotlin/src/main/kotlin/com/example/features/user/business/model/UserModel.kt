package com.example.features.user.business.model

import com.example.util.enums.*
import com.example.util.extensions.*
import com.typesafe.config.*
import io.ktor.server.plugins.*
import kotlinx.serialization.*
import org.mindrot.jbcrypt.*

/**
 * UserBusiness model
 *
 * @author Reshwan Barhoe
 */
@Serializable
data class UserModel(
    private val id: Int? = null,
    private val firstName: String,
    private val prefixes: String?,
    private val lastName: String,
    private val role: Roles,
    private val email: String,
    private val password: String
) {

    /**
     * Second constructor of the user model
     * @param firstName the first name
     * @param prefixes the prefixes
     * @param lastName the last name
     * @param email the email
     * @param role the role
     * @author Ömer Aynaci
     */
    constructor(
        firstName: String,
        prefixes: String?,
        lastName: String,
        email: String,
        role: Roles
    ) : this(null, firstName, prefixes, lastName, role, email, "")

    /**
     * Third constructor of the user model. This is used for the update password
     * @author Ömer Aynaci
     * @param password the password
     */
    constructor(
        password: String
    ) : this(null, "", "", "", Roles.MEMBER, "", password)

    private val config: Config = ConfigFactory.load("applicationRegex.conf")
    private val emailConfig: String = config.getString("ktor.register.email.emailRegex")
    private val passwordConfig: String =  config.getString("ktor.register.password.passwordRegex")


    /**
     * email requirements:
     * Email must have a username.
     * Email must have @ sign.
     * Email must have provider name after @ sign.
     * Email must have domain name after dot.
     * @author Ömer Aynaci
     */
    @Contextual
    private val emailRegex = Regex(emailConfig)

    /**
     * password requirements:
     * password must contain at least 1 lowercase letter
     * password must contain at least 1 uppercase letter
     * password must contain at least 1 digit
     * password must contain at least 1 special character
     * @author Ömer Aynaci
     */
    @Contextual
    private val passwordRegex = Regex(passwordConfig)

    fun getFirstName(): String {
        return firstName
    }

    fun getLastName(): String {
        return lastName
    }

    fun getPrefixes(): String? {
        return prefixes
    }

    fun getEmail(): String {
        return email
    }

    fun getEmailConfig(): String {
        return this.emailConfig
    }

    /**
     * hashes the password
     * @author Ömer Aynaci
     * @return the hashed password
     */
    fun hashPassword(): String {
        val salt = BCrypt.gensalt()
        return BCrypt.hashpw(password, salt)
    }

    fun getHashedPassword(): String {
        return this.password
    }

    fun getUserId(): Int? {
        return this.id
    }

    fun getRole(): Roles {
        return this.role
    }

    /**
     * checks if the first name is valid
     * @author Ömer Aynaci
     * @return true if the first name is valid otherwise throws an error
     */
    private fun validateFirstName() {
        val validateFirstName = this.firstName.isLengthValid(2, 255)
        if (!validateFirstName) {
            throw BadRequestException(ErrorCode.U_INVALID_LENGTH_FIRST_NAME.code)
        }
    }

    /**
     * checks if the last name is valid
     * @author Ömer Aynaci
     * @return true if the last name is valid otherwise throws an error
     */
    private fun validateLastName() {
        val validateLastName = this.lastName.isLengthValid(2, 255)
        if (!validateLastName) {
            throw BadRequestException(ErrorCode.U_INVALID_LENGTH_LAST_NAME.code)
        }
    }

    /**
     * checks if the prefixes is valid
     * @author Ömer Aynaci
     * @return true if the prefixes is valid otherwise throws an error
     */
    private fun validatePrefixes() {
        val validatePrefixes = this.prefixes?.isLengthValid(0, 20)
        if (validatePrefixes != null && !validatePrefixes) {
            throw BadRequestException(ErrorCode.U_INVALID_LENGTH_PREFIXES.code)
        }
    }

    /**
     * checks if the email is valid
     * @author Ömer Aynaci
     * @return true if the email is valid otherwise throws an error
     */
    private fun validateEmail() {
        if (!this.email.matches(emailRegex)) {
            throw BadRequestException(ErrorCode.U_INVALID_EMAIL.code)

        }
    }

    /**
     * checks if the password is valid
     * @author Ömer Aynaci
     * @return true if the password is valid otherwise throws an error
     */
    fun validatePassword() {
        if (!this.password.matches(passwordRegex)) {
            throw BadRequestException(ErrorCode.U_INVALID_PASSWORD.code)
        }
    }

    /**
     * validates the role
     * @author Ömer Aynaci
     * @throws BadRequestException if a role doesn't exists in the Roles enum
     */
    private fun validateRole() {
        if (this.role !in Roles.entries) {
            throw BadRequestException(ErrorCode.U_INVALID_ROLE.code)
        }
    }

    /**
     * validates the user inputs of registration
     * @author Ömer Aynaci
     */
    fun validateUserInputs() {
        validateFirstName()
        validateLastName()
        validatePrefixes()
        validateEmail()
        validatePassword()
    }

    /**
     * validates the user inputs of updating user details
     * @author Ömer Aynaci
     */
    fun validateUpdateUser() {
        validateFirstName()
        validateLastName()
        validatePrefixes()
        validateEmail()
        validateRole()
    }

    /**
     * checks if the plain password with the hashed password are matching
     * @author Ömer Aynaci
     * @param hashedPassword the hashed password
     * @return true if the plain password matches the hashed password
     */
    fun doesPasswordMatch(oldPassword: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(oldPassword, hashedPassword)
    }
}