package com.example.user.routing


import com.example.features.session.data.model.*
import com.example.features.user.data.model.*
import com.example.features.user.dtos.*
import com.example.general.exception.*
import com.example.user.helpers.*
import com.example.util.*
import com.example.util.enums.*
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import jdk.jfr.*
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import java.time.*
import kotlin.test.*

class UserRoutingTest {


    @BeforeTest
    fun setup() {
        testApplication {
            DatabaseFactory.init()
            transaction {
                SchemaUtils.create(User, Sessions)
                UserTestHelper.provideUserSearchData()

                Sessions.insert {
                    it[userId] = 7
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
                SchemaUtils.drop(User, Sessions)
                rollback()
            }
        }
    }

    /**
     * Returns a client for testing
     * @author Reshwan Barhoe
     * @return a client
     */
    private fun ApplicationTestBuilder.getClient(): HttpClient {
        return createClient {
            install(ContentNegotiation) {
                json()
            }
        }
    }

    @Test
    fun `GET request should respond with a status-code 200 with the expected body`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = getClient()
        val response = client.get("users/1")
        val expectedBody = UserDetails(
            "Henk",
            "van",
            "Jongen",
            "henkiscool@gmail.com"
        )
        val expectedStatus = HttpStatusCode.OK
        assertEquals(Json.encodeToString(expectedBody), response.bodyAsText())
        assertEquals(response.status, expectedStatus)
    }

    @Test
    fun `GET request should respond with a status 404 'User not Found' `() = runBlocking {
        testApplication {
            environment {
                config = ApplicationConfig("testApplication.conf")
            }
            val client = getClient()
            val nonExistingUserId = Int.MAX_VALUE
            val response = client.get("users/$nonExistingUserId")
            val expectedBody = ResponseMessage(ErrorCode.U_NOT_FOUND.code)
            val expectedStatus = HttpStatusCode.NotFound
            assertEquals(Json.encodeToString(expectedBody), response.bodyAsText())
            assertEquals(expectedStatus, response.status)
        }
    }

    @Test
    fun `GET request should respond with a status 422 'Numeric ID required' `() = runBlocking {
        testApplication {
            environment {
                config = ApplicationConfig("testApplication.conf")
            }
            val client = getClient()
            val nonNumericId = "notANumber"
            val response = client.get("users/$nonNumericId")
            val expectedBody = ResponseMessage(ErrorCode.G_NUMERIC_ID_REQUIRED.code)
            val expectedStatus = HttpStatusCode.UnprocessableEntity
            assertEquals(Json.encodeToString(expectedBody), response.bodyAsText())
            assertEquals(expectedStatus, response.status)
        }
    }

    @Test
    fun `GET request for searching should respond with a status 200 and a list with users`() = runBlocking {
        testApplication {
            environment {
                config = ApplicationConfig("testApplication.conf")
            }
            val client = getClient()
            val searchTerm = "b"
            val response = client.get("users?name=$searchTerm")
            val expected = UserTestHelper.convertAndOrderToUserSummary(
                listOf(
                    TestUser(4, "Grietje", "den", "Bos"),
                    TestUser(2, "Pieter", "", "Brood")
                )
            )
            val expectedStatus = HttpStatusCode.OK
            assertEquals(Json.encodeToString(expected), response.bodyAsText())
            assertEquals(expectedStatus, response.status)
        }
    }

    @Test
    @Description("GET request for searching should respond status 200 and a empty array")
    fun `If no data is found for the provided searchTerm`() = runBlocking {
        testApplication {
            environment {
                config = ApplicationConfig("testApplication.conf")
            }
            val client = getClient()
            val searchTerm = "NotFound"
            val response = client.get("users?name=$searchTerm")
            val expectedBody = emptyList<UserSummary>()
            val expectedStatus = HttpStatusCode.OK
            assertEquals(Json.encodeToString(expectedBody), response.bodyAsText())
            assertEquals(expectedStatus, response.status)
        }
    }

    @Test
    @Description("GET request for searching should respond status 200 and a list of users")
    fun `If there is no provided searchTerm value`() = runBlocking {
        testApplication {
            environment {
                config = ApplicationConfig("testApplication.conf")
            }
            val client = getClient()
            val searchTerm = ""
            val response = client.get("users?name=$searchTerm")
            val expectedBody = UserTestHelper.getAllTestUsers()
            val expectedStatus = HttpStatusCode.OK
            assertEquals(Json.encodeToString(expectedBody), response.bodyAsText())
            assertEquals(expectedStatus, response.status)
        }
    }

    @Test
    @Description("GET request for searching should respond status 400 with errorCode 11")
    fun `If the query param contains a special character`() = runBlocking {
        testApplication {
            environment {
                config = ApplicationConfig("testApplication.conf")
            }
            val client = getClient()
            val searchTerm = "b!"
            val response = client.get("users?name=$searchTerm")
            val expectedBody = ResponseMessage(ErrorCode.G_INVALID_QUERY_CHARACTER.code)
            val expectedStatus = HttpStatusCode.BadRequest
            assertEquals(Json.encodeToString(expectedBody), response.bodyAsText())
            assertEquals(expectedStatus, response.status)
        }
    }

    @Test
    fun `should throw BadRequestException if first name has less than 2 characters`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }
        val client = getClient()
        val expectedBody =
            CreateUserDTO("K", "Kaas", "van den", "keeskaas@nl.abnamro.com", "Password12345$")
        val response = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(expectedBody))
        }

        val expectedStatusCode = HttpStatusCode.BadRequest
        val expectedResponse = ResponseMessage(ErrorCode.U_INVALID_LENGTH_FIRST_NAME.code)
        assertEquals(expectedStatusCode, response.status)
        assertEquals(Json.encodeToString(expectedResponse), response.bodyAsText())
    }

    @Test
    fun `should throw BadRequestException if first name has more than 255 characters`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }
        val client = getClient()
        val firstNameExceededLimit = "Kees".repeat(260)
        val expectedBody =
            CreateUserDTO(
                firstNameExceededLimit,
                "Kaas",
                "van den",
                "keeskaas@nl.abnamro.com",
                "Password12345$"
            )
        val response = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(expectedBody))
        }

        val expectedStatusCode = HttpStatusCode.BadRequest
        val expectedResponse = ResponseMessage(ErrorCode.U_INVALID_LENGTH_FIRST_NAME.code)
        assertEquals(expectedStatusCode, response.status)
        assertEquals(Json.encodeToString(expectedResponse), response.bodyAsText())
    }

    @Test
    fun `should throw BadRequestException if last name has less than 2 characters`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }
        val client = getClient()
        val expectedBody =
            CreateUserDTO("Kees", "K", "van den", "keeskaas@nl.abnamro.com", "Password12345$")
        val response = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(expectedBody))
        }

        val expectedStatusCode = HttpStatusCode.BadRequest
        val expectedResponse = ResponseMessage(ErrorCode.U_INVALID_LENGTH_LAST_NAME.code)
        assertEquals(expectedStatusCode, response.status)
        assertEquals(Json.encodeToString(expectedResponse), response.bodyAsText())
    }

    @Test
    fun `should throw BadRequestException if last name has more than 255 characters`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }
        val client = getClient()
        val lastNameExceededLimit = "Kaas".repeat(260)
        val expectedBody =
            CreateUserDTO(
                "Kees",
                lastNameExceededLimit,
                "van den",
                "keeskaas@nl.abnamro.com",
                "Password12345$"
            )
        val response = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(expectedBody))
        }

        val expectedStatusCode = HttpStatusCode.BadRequest
        val expectedResponse = ResponseMessage(ErrorCode.U_INVALID_LENGTH_LAST_NAME.code)
        assertEquals(expectedStatusCode, response.status)
        assertEquals(Json.encodeToString(expectedResponse), response.bodyAsText())
    }

    @Test
    fun `should throw BadRequestException if prefixes has more than 20 characters`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }
        val client = getClient()
        val prefixesExceededLimit = "van den".repeat(50)
        val expectedBody =
            CreateUserDTO(
                "Kees",
                "Kaas",
                prefixesExceededLimit,
                "keeskaas@nl.abnamro.com",
                "Password12345$"
            )
        val response = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(expectedBody))
        }

        val expectedStatusCode = HttpStatusCode.BadRequest
        val expectedResponse = ResponseMessage(ErrorCode.U_INVALID_LENGTH_PREFIXES.code)
        assertEquals(expectedStatusCode, response.status)
        assertEquals(Json.encodeToString(expectedResponse), response.bodyAsText())
    }

    @Test
    fun `should throw BadRequestException if email address is invalid`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }
        val client = getClient()

        val expectedBody = CreateUserDTO("Kees", "Kaas", "van den", "keeskaas@nl", "Password12345$")

        val response = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(expectedBody))
        }

        val expectedStatusCode = HttpStatusCode.BadRequest
        val expectedResponse = ResponseMessage(ErrorCode.U_INVALID_EMAIL.code)
        assertEquals(expectedStatusCode, response.status)
        assertEquals(Json.encodeToString(expectedResponse), response.bodyAsText())
    }

    @Test
    fun `should throw EntityExistsException if email already exists`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }
        val client = getClient()

        val expectedBody = CreateUserDTO("Kees", "Kaas", "van den", "henkiscool@gmail.com", "Password12345$")

        val response = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(expectedBody))
        }

        val expectedStatusCode = HttpStatusCode.Conflict
        val expectedResponse = ResponseMessage(ErrorCode.U_EMAIL_EXISTS.code)
        assertEquals(expectedStatusCode, response.status)
        assertEquals(Json.encodeToString(expectedResponse), response.bodyAsText())
    }

    @Test
    fun `update user should response with a status code 200 with expected body`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val expectedBody = UpdateUserDTO("Henk", "Jongen", "van", "henkvanjongen@nl.abnamro.com", Roles.MANAGER)
        val response = client.put("/users") {
            contentType(ContentType.Application.Json)
            setBody(expectedBody)
            bearerAuth(UserTestHelper.TOKEN)
        }

        val expectedStatusCode = HttpStatusCode.OK
        assertEquals(expectedStatusCode, response.status)
    }

    @Test
    fun `should throw BadRequestException if first name update is invalid`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val expectedBody = UpdateUserDTO("H", "Jongen", "van", "henkiscool@gmail.com", Roles.MANAGER)
        val response = client.put("/users") {
            contentType(ContentType.Application.Json)
            setBody(expectedBody)
            bearerAuth(UserTestHelper.TOKEN)
        }

        val expectedStatusCode = HttpStatusCode.BadRequest
        val expectedError = ResponseMessage(ErrorCode.U_INVALID_LENGTH_FIRST_NAME.code)
        assertEquals(expectedStatusCode, response.status)
        assertEquals(Json.encodeToString(expectedError), response.bodyAsText())
    }

    @Test
    fun `should throw BadRequestException if last name update is invalid`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val expectedBody =
            UpdateUserDTO("Henk", "Jongen".repeat(256), "van", "henkvanjongen@nl.abnamro.com", Roles.MANAGER)
        val response = client.put("/users") {
            contentType(ContentType.Application.Json)
            setBody(expectedBody)
            bearerAuth(UserTestHelper.TOKEN)
        }

        val expectedStatusCode = HttpStatusCode.BadRequest
        val expectedError = ResponseMessage(ErrorCode.U_INVALID_LENGTH_LAST_NAME.code)
        assertEquals(expectedStatusCode, response.status)
        assertEquals(Json.encodeToString(expectedError), response.bodyAsText())
    }

    @Test
    fun `should throw BadRequestException if prefixes update is invalid`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val expectedBody =
            UpdateUserDTO("Henk", "Jongen", "van".repeat(50), "henkvanjongen@nl.abnamro.com", Roles.MANAGER)
        val response = client.put("/users") {
            contentType(ContentType.Application.Json)
            setBody(expectedBody)
            bearerAuth(UserTestHelper.TOKEN)
        }

        val expectedStatusCode = HttpStatusCode.BadRequest
        val expectedError = ResponseMessage(ErrorCode.U_INVALID_LENGTH_PREFIXES.code)
        assertEquals(expectedStatusCode, response.status)
        assertEquals(Json.encodeToString(expectedError), response.bodyAsText())
    }

    @Test
    fun `should throw BadRequestException if email update is invalid`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val expectedBody = UpdateUserDTO("Henk", "Jongen", "van", "henkvanjongennl.abnamro.", Roles.MANAGER)
        val response = client.put("/users") {
            contentType(ContentType.Application.Json)
            setBody(expectedBody)
            bearerAuth(UserTestHelper.TOKEN)
        }

        val expectedStatusCode = HttpStatusCode.BadRequest
        val expectedError = ResponseMessage(ErrorCode.U_INVALID_EMAIL.code)
        assertEquals(expectedStatusCode, response.status)
        assertEquals(Json.encodeToString(expectedError), response.bodyAsText())
    }

    @Test
    fun `should throw BadRequestException if role update is invalid`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val expectedBody =
            "{\"firstName\": \"Henk\", \"lastName\": \"Kees\",\"prefixes\": \"van den\", \"email\": \"henkvanjongen@nl.abnamro.com\", \"role\": \"Kees\"}"
        val response = client.put("/users") {
            contentType(ContentType.Application.Json)
            setBody(expectedBody)
            bearerAuth(UserTestHelper.TOKEN)
        }

        val expectedStatusCode = HttpStatusCode.BadRequest
        val expectedError = ResponseMessage(ErrorCode.G_INVALID_REQUEST_BODY.code)
        assertEquals(expectedStatusCode, response.status)
        assertEquals(Json.encodeToString(expectedError), response.bodyAsText())
    }

    @Test
    fun `should throw Unauthorized exception if old password doesn't matches the password that is stored in the database`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val requestBody = UpdatePasswordDTO("Password1234$123456", "Voetbal11@")
        val response = client.put("/users/password") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
            bearerAuth(UserTestHelper.TOKEN)
        }

        val expectedStatusCode = HttpStatusCode.Unauthorized
        val expectedError = ResponseMessage(ErrorCode.U_INCORRECT_PASSWORD.code)
        assertEquals(expectedStatusCode, response.status)
        assertEquals(Json.encodeToString(expectedError), response.bodyAsText())
    }

    @Test
    fun `should throw BadRequestException if new password is equal to the old password`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val requestBody = UpdatePasswordDTO("Password1234\$", "Password1234\$")
        val response = client.put("/users/password") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
            bearerAuth(UserTestHelper.TOKEN)
        }

        val expectedStatusCode = HttpStatusCode.BadRequest
        val expectedError = ResponseMessage(ErrorCode.U_NEW_PASSWORD_EQUAL_TO_OLD.code)
        assertEquals(expectedStatusCode, response.status)
        assertEquals(Json.encodeToString(expectedError), response.bodyAsText())
    }


    @Test
    fun `should throw BadRequestException if the new password is invalid`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val requestBody = UpdatePasswordDTO("Password1234\$", "short")
        val response = client.put("/users/password") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
            bearerAuth(UserTestHelper.TOKEN)
        }

        val expectedStatusCode = HttpStatusCode.BadRequest
        val expectedError = ResponseMessage(ErrorCode.U_INVALID_PASSWORD.code)
        assertEquals(expectedStatusCode, response.status)
        assertEquals(Json.encodeToString(expectedError), response.bodyAsText())
    }

    @Test
    fun `should update password with response status 200`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val requestBody = UpdatePasswordDTO("Password1234\$", "Voetbal11@")
        val response = client.put("/users/password") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
            bearerAuth(UserTestHelper.TOKEN)
        }

        val expectedStatusCode = HttpStatusCode.OK
        assertEquals(expectedStatusCode, response.status)
    }
}