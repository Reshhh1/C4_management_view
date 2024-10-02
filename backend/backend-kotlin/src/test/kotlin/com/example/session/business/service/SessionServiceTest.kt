package com.example.session.business.service

import com.example.features.session.authentication.UserAuthenticator
import com.example.features.session.data.model.Sessions
import com.example.features.session.util.di.components.DaggerSessionHandlerComponent
import com.example.features.user.data.model.User
import com.example.general.exception.UnauthorizedError
import com.example.util.DatabaseFactory
import com.example.util.enums.Roles
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mindrot.jbcrypt.BCrypt
import org.mockito.InjectMocks
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class SessionServiceTest {

    @InjectMocks
    private var mockSessionService = DaggerSessionHandlerComponent
        .create()
        .getSessionService()

    @BeforeTest
    fun setup() {
        testApplication {
            DatabaseFactory.init()
            transaction {
                SchemaUtils.create(User, Sessions)
                User.insert {
                    it[firstName] = "Henk"
                    it[prefixes] = "van"
                    it[lastName] = "Jongen"
                    it[email] = "henkiscool@gmail.com"
                    it[role] = Roles.MEMBER
                    it[password] = BCrypt.hashpw("password123!", BCrypt.gensalt())
                }
            }
        }
    }

    @AfterTest
    fun cleanup() {
        runBlocking {
            transaction {
                SchemaUtils.drop(Sessions, User)
                rollback()
            }
        }
    }

    @Test
    fun `should return true if plain password and hashed password are identical`() = runBlocking {
        testApplication {
            val email = "henkiscool@gmail.com"
            val password = "password123!"
            val expected = UserAuthenticator(email, password)
            assertDoesNotThrow {
                mockSessionService.passwordIsIdentical(expected)
            }
        }
    }

    @Test
    fun `user logs in with correct credentials`() = runBlocking {
        testApplication {

            val email = "henkiscool@gmail.com"
            val password = "password123!"
            val expected = UserAuthenticator(email, password)

            assertDoesNotThrow {
                mockSessionService.login(expected)
            }
        }
    }

    @Test
    fun `should throw a Invalid credentials error if password is incorrect`() = runBlocking {
        testApplication {

            val email = "henkiscool@gmail.com"
            val password = "password"
            val expected = UserAuthenticator(email, password)
            assertThrows<UnauthorizedError> {
                mockSessionService.login(expected)
            }
        }
    }
}