package com.example.features.session.business.service

import com.example.features.session.authentication.*
import com.example.features.session.data.repository.*
import com.example.features.user.business.model.*
import com.example.features.user.data.repository.*
import com.example.general.exception.*
import com.example.util.enums.*
import io.ktor.server.plugins.*
import java.time.*
import javax.inject.*

class SessionService @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
) {

    /**
     * logging a user in
     *
     * @param userAuthenticator the user authenticator
     * @return true if the credentials are valid otherwise false
     * @author Ömer Aynaci
     */
    suspend fun login(userAuthenticator: UserAuthenticator): String {
        val userModel = getUserByEmail(userAuthenticator.email)
        val jwtToken = createToken(userAuthenticator.email)
        passwordIsIdentical(userAuthenticator)
        if (isLoginSuccessful(userAuthenticator)) {
            sessionRepository.createSession(
                jwtToken,
                userModel.getUserId()!!,
                LocalDateTime.now().plusWeeks(1)
            )
        }
        return jwtToken
    }



    /**
     * gets the email of the user
     *
     * @param email the email
     * @throws UnauthorizedError if the email doesn't exists
     * @author Ömer Aynaci & Reshwan Barhoe
     */
    private suspend fun getUserByEmail(email: String): UserModel {
        return userRepository.getUserByEmail(email) ?: throw UnauthorizedError(ErrorCode.G_UNAUTHORIZED.code)
    }

    /**
     * creates the token
     * @param email the email
     * @return the token
     * @author Ömer Aynaci
     */
    private fun createToken(email: String): String {
        val jwtToken = JWTAuthentication(userRepository).generateToken(email)
        return jwtToken
    }


    /**
     * checking if login is successful
     * @param userAuthenticator the user authenticator
     * @return true if user credentials are valid otherwise false
     * @author Ömer Aynaci
     */
    private suspend fun isLoginSuccessful(userAuthenticator: UserAuthenticator): Boolean {
        return validateUserCredentials(userAuthenticator)
    }

    /**
     * Checking if the password is identical
     * @param userAuthenticator the user authenticator
     * @return true if the plain password and the hashed password are matching otherwise throw invalid credentials error
     * @author Ömer Aynaci
     */
    suspend fun passwordIsIdentical(userAuthenticator: UserAuthenticator) {
        val password = userRepository.getPasswordByEmail(userAuthenticator.email)
        if (password != null) {
            userAuthenticator.validatePasswordForAuthentication(password)
        }
    }

    /**
     * validates the user credentials
     * @param userAuthenticator the login body
     * @throws BadRequestException if email doesn't match with the stored password otherwise true
     * @author Ömer Aynaci
     */
    private suspend fun validateUserCredentials(userAuthenticator: UserAuthenticator): Boolean {
        val userIdAndPassword = userRepository.getPasswordByEmail(userAuthenticator.email)
        val doesEmailExists = userRepository.getEmail(userAuthenticator.email)
        val checkPassword = userAuthenticator.doesPasswordMatch(userIdAndPassword!!)
        if (doesEmailExists == checkPassword) {
            return true
        } else {
            throw BadRequestException(ErrorCode.U_INVALID_CREDENTIALS.code)
        }
    }
}