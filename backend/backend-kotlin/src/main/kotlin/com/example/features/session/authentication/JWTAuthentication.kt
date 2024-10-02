package com.example.features.session.authentication

import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import com.example.features.user.data.repository.*
import com.example.util.*
import com.typesafe.config.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.*

class JWTAuthentication (private val userRepository: UserRepository) {
    private val config: Config = DatabaseFactory.getConfig()
    private val audience = config.getString("ktor.jwt.audience")
    private val issuer = config.getString("ktor.jwt.issuer")
    private val secret = config.getString("ktor.jwt.secret")

    /**
     * gets the jwt token
     * @return the generated token
     * @author Ömer Aynaci
     */
    fun generateToken(email: String): String {
        val currentTimeMillis = System.currentTimeMillis()
        val oneWeek = 7 * 24 * 60 * 60 * 1000
        val expireDate = currentTimeMillis + oneWeek
        return JWT.create().withAudience(audience).withIssuer(issuer).withClaim("email", email)
            .withExpiresAt(Date(expireDate)).sign(Algorithm.HMAC256(secret))
    }

    /**
     * verifies the created token
     * @author Ömer Aynaci
     * @return JWTVerifier instance
     */
    fun verifyToken(): JWTVerifier {
        return JWT
            .require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
    }


    /**
     * authorizing a user when login is successful
     * @author Ömer Aynaci
     * @param credential the JWTCredential class instance
     * @return a principal for authorizing a user
     */
    suspend fun authorizeUser(credential: JWTCredential): Principal {
        val email = credential.payload.getClaim("email").asString()
        val expirationTime = credential.payload.expiresAt?.time
        val userId = userRepository.getUserIdByEmail(email)
        if (email != null && userId != null && expirationTime != null && expirationTime > System.currentTimeMillis()) {
            AuthenticatedUser(userId, email)
        }
        return JWTPrincipal(credential.payload)
    }

}