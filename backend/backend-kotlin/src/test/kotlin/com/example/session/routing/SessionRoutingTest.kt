package com.example.session.routing

import com.example.features.session.data.model.*
import com.example.features.session.dtos.*
import com.example.features.user.data.model.*
import com.example.general.exception.*
import com.example.user.helpers.*
import com.example.util.*
import com.example.util.enums.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.junit.Test
import org.mindrot.jbcrypt.*
import java.time.*
import kotlin.test.*

class SessionRoutingTest {

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
                    it[role] = Roles.MEMBER
                    it[email] = "henkvanjongen@nl.abnamro.com"
                    it[password] = BCrypt.hashpw("password123!", BCrypt.gensalt())
                }

                Sessions.insert {
                    it[userId] = 1
                    it[token] = UserTestHelper.TOKEN
                    it[createdAt] = LocalDateTime.now()
                    it[expireDate] = LocalDateTime.now().plusWeeks(1)
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
    fun `user is authenticated but is unauthorized and should response with a status code 401 with the expected body`() =
        testApplication {
            environment {
                config = ApplicationConfig("testApplication.conf")
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            val token =
                "sInR5cCI6IkpXVCJ9.eyJhdWQiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvaGVsbG8iLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvIiwiZW1haWwiOiJoZW5rdmFuam9uZ2VuQG5sLmFibmFtcm8uY29tIiwiZXhwIjoxNzExOTcxNzg4fQ.ZMWunQ3jjtQXA63oMwpiFi0gUv_kfqbUOsMGkiitIcA"
            val response = client.get("/users/firstname") {
                contentType(ContentType.Application.Json)
                bearerAuth(token)
            }

            val expectedStatus = HttpStatusCode.Unauthorized
            val expectedBody = ResponseMessage(ErrorCode.G_SESSION_EXPIRED.code)
            assertEquals(expectedStatus, response.status)
            assertEquals(Json.encodeToString(expectedBody), response.bodyAsText())
        }


    @Test
    fun `user is authenticated and should get a response with a status of 200 and the expected body`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/users/firstname") {
            contentType(ContentType.Application.Json)
            bearerAuth(UserTestHelper.TOKEN)
        }

        val expectedStatus = HttpStatusCode.OK
        val expectedBody = DashboardMessage("Henk")
        assertEquals(expectedStatus,response.status)
        assertEquals(Json.encodeToString(expectedBody), response.bodyAsText())
    }



    @Test
    fun `login should response with a status code 200 with the expected body`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val expectedBody = "{\"email\": \"henkvanjongen@nl.abnamro.com\", \"password\": \"password123!\"}"
        val response = client.post("/sessions") {
            contentType(ContentType.Application.Json)
            setBody(expectedBody)
        }
        val expectedStatus = HttpStatusCode.OK
        assertEquals(expectedStatus, response.status)
    }


    @Test
    fun `should respond with unauthorized and response status code should be status code 401`() = testApplication {
        testApplication {
            environment {
                config = ApplicationConfig("testApplication.conf")
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            val token =
                "fwfewewffewfeeyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvaGVsbG8iLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvIiwiZW1haWwiOiJoZW5rdmFuam9uZ2VuQG5sLmFibmFtcm8uY29tIiwiZXhwIjoxNzExOTcxNzg4fQ.ZMWunQ3jjtQXA63oMwpiFi0gUv_kfqbUOsMGkiitIcA"
            val response = client.get("/users/firstname") {
                contentType(ContentType.Application.Json)
                bearerAuth(token)
            }
            val expectedStatus = HttpStatusCode.Unauthorized
            val expectedBody = ResponseMessage(ErrorCode.G_SESSION_EXPIRED.code)
            val json = Json.encodeToString(expectedBody)
            assertEquals(response.status, expectedStatus)
            assertEquals(json, response.bodyAsText())
        }
    }

    @Test
    fun `login should response with a status code 401 for invalid password with expected error code`() =
        testApplication {
            environment {
                config = ApplicationConfig("testApplication.conf")
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            val expectedBody = "{\"email\": \"henkiscool@gmail.com\", \"password\": \"password123!ssss\"}"
            val response = client.post("/sessions") {
                contentType(ContentType.Application.Json)
                setBody(expectedBody)
            }
            val expectedStatus = HttpStatusCode.Unauthorized
            val expectedErrorCode = ResponseMessage(ErrorCode.G_UNAUTHORIZED.code)
            assertEquals(expectedStatus, response.status)
            assertEquals(Json.encodeToString(expectedErrorCode), response.bodyAsText())
        }

    @Test
    fun `login should response with a status code 401 with expected error code`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val expectedBody = "{\"email\": \"keeskaas@gmail.com\", \"password\": \"password123!\"}"
        val response = client.post("/sessions") {
            contentType(ContentType.Application.Json)
            setBody(expectedBody)
        }
        val expectedStatus = HttpStatusCode.Unauthorized
        val expectedErrorCode = ResponseMessage(ErrorCode.G_UNAUTHORIZED.code)
        assertEquals(expectedStatus, response.status)
        assertEquals(Json.encodeToString(expectedErrorCode), response.bodyAsText())
    }
}
