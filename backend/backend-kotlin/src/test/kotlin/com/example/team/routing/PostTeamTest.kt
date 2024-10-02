package com.example.team.routing

import com.example.features.team.data.model.Team
import com.example.features.team.data.model.TeamUser
import com.example.features.team.dtos.CreateTeamDTO
import com.example.features.user.data.model.User
import com.example.general.exception.ResponseMessage
import com.example.team.helpers.TeamTestHelper
import com.example.util.DatabaseFactory
import com.example.util.enums.ErrorCode
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class PostTeamTest {
    @BeforeTest
    fun setup() {
        testApplication {
            DatabaseFactory.init()
            transaction {
                SchemaUtils.create(User, Team, TeamUser)
                TeamTestHelper.provideTeamPOSTData()
            }
        }
    }

    @AfterTest
    fun cleanup() {
        runBlocking {
            transaction {
                SchemaUtils.drop(TeamUser, Team, User)
                rollback()
            }
        }
    }

    @Test
    fun `should respond with a status-code 200 if the provided body is valid`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val body = CreateTeamDTO(
            "Team1",
            listOf(1,2,3)
        )
        val response = client.post("teams") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

        val expectedStatus = HttpStatusCode.Created
        assertEquals(response.status, expectedStatus)
    }

    @Test
    fun `should respond with status 400 and errorCode 31 if the team name is too short`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val body = CreateTeamDTO(
            "e",
            listOf(1,2,3)
        )
        val response = client.post("teams") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

        val expectedStatus = HttpStatusCode.BadRequest
        val expectedErrorCode = ResponseMessage(ErrorCode.T_INVALID_NAME_LENGTH.code)

        assertEquals(response.status, expectedStatus)
        assertEquals(Json.encodeToString(expectedErrorCode), response.bodyAsText())
    }

    @Test
    fun `should respond with status 400 and errorCode 31 if the team name starts with a number`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val body = CreateTeamDTO(
            "1e",
            listOf(1,2,3)
        )
        val response = client.post("teams") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

        val expectedStatus = HttpStatusCode.BadRequest
        val expectedErrorCode = ResponseMessage(ErrorCode.T_INVALID_STARTING_CHARACTER.code)

        assertEquals(response.status, expectedStatus)
        assertEquals(Json.encodeToString(expectedErrorCode), response.bodyAsText())
    }

    @Test
    fun `should respond with status 404 and errorCode 31 if a given member doesn't exists`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val notExistingId = 13
        val body = CreateTeamDTO(
            "BugMites",
            listOf(1,2,notExistingId)
        )
        val response = client.post("teams") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

        val expectedStatus = HttpStatusCode.NotFound
        val expectedErrorCode = ResponseMessage(ErrorCode.T_MEMBER_NOT_FOUND.code)

        assertEquals(response.status, expectedStatus)
        assertEquals(Json.encodeToString(expectedErrorCode), response.bodyAsText())
    }

    @Test
    fun `should respond with status 400 and errorCode 33 if there are no member ids`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val body = CreateTeamDTO(
            "BugMites",
            listOf()
        )
        val response = client.post("teams") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

        val expectedStatus = HttpStatusCode.BadRequest
        val expectedErrorCode = ResponseMessage(ErrorCode.T_MEMBER_IS_EMPTY.code)

        assertEquals(response.status, expectedStatus)
        assertEquals(Json.encodeToString(expectedErrorCode), response.bodyAsText())
    }

    @Test
    fun `should respond with status 409 and errorCode 34 if the team already exists`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val existingTeamName = "FireBeats"
        val body = CreateTeamDTO(
            existingTeamName,
            listOf(1)
        )
        val response = client.post("teams") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

        val expectedStatus = HttpStatusCode.Conflict
        val expectedErrorCode = ResponseMessage(ErrorCode.T_NAME_EXISTS.code)

        assertEquals(response.status, expectedStatus)
        assertEquals(Json.encodeToString(expectedErrorCode), response.bodyAsText())
    }
}