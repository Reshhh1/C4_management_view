package com.example.team.routing

import com.example.features.team.data.model.Team
import com.example.features.team.data.model.TeamUser
import com.example.features.team.dtos.TeamDetails
import com.example.features.team.dtos.TeamSummary
import com.example.features.user.data.model.User
import com.example.features.user.dtos.UserSummary
import com.example.general.exception.ResponseMessage
import com.example.team.helpers.TeamTestHelper
import com.example.util.DatabaseFactory
import com.example.util.enums.ErrorCode
import io.ktor.client.*
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
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class GetTeamTest {

    @BeforeTest
    fun setup() {
        testApplication {
            DatabaseFactory.init()
            transaction{
                SchemaUtils.create(User, Team, TeamUser)
                TeamTestHelper.provideTeamGETData()
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
    fun `should respond with a status-code 200 with a list of teams if the database contains teams data`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }

        val client = getClient()
        val expectedBody = listOf(
            TeamSummary(1, "Bug Bytes"),
            TeamSummary(2, "AppelHuis"),
            TeamSummary(3, "Fire Mites"),
            TeamSummary(4, "EasyCoding"),
            TeamSummary(5, "FireBeats")
        ).sortedBy { it.name }
        val response = client.get("teams")
        val expectedStatus = HttpStatusCode.OK

        assertEquals(response.status, expectedStatus)
        assertEquals(Json.encodeToString(expectedBody), response.bodyAsText())
    }

    @Test
    fun `should respond with a status-code 200 with a empty array if the database contains no teams data`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }
        val client = getClient()
        transaction { TeamUser.deleteAll() }
        transaction { Team.deleteAll() }

        val response = client.get("teams")
        val expectedStatus = HttpStatusCode.OK
        val expectedBody = "[]"
        assertEquals(response.status, expectedStatus)
        assertEquals(expectedBody, response.bodyAsText())
    }

    @Test
    fun `should respond with a status-code 400 if the provided id isn't a int`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }
        val client = getClient()
        val response = client.get("teams/NotAInt")
        val expectedStatus = HttpStatusCode.UnprocessableEntity
        val expectedBody = ResponseMessage(ErrorCode.G_NUMERIC_ID_REQUIRED.code)
        assertEquals(response.status, expectedStatus)
        assertEquals(Json.encodeToString(expectedBody), response.bodyAsText())
    }

    @Test
    fun `should respond with a status-code 404 if the provided id doesn't exists`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }
        val client = getClient()

        val response = client.get("teams/100000")
        val expectedStatus = HttpStatusCode.NotFound
        val expectedBody = ResponseMessage(ErrorCode.T_NOT_FOUND.code)
        assertEquals(response.status, expectedStatus)
        assertEquals(Json.encodeToString(expectedBody), response.bodyAsText())
    }
    @Test
    fun `should respond with a status-code 200 with the expected body if the id exists`() = testApplication {
        environment {
            config = ApplicationConfig("testApplication.conf")
        }
        val client = getClient()

        val response = client.get("teams/1")
        val expectedStatus = HttpStatusCode.OK
        val expectedBody = TeamDetails(
            "Bug Bytes",
            listOf(
                UserSummary(1, "Henk", "van", "Jongen")
            ),
            TeamTestHelper.createdAtTime
        )
        assertEquals(response.status, expectedStatus)
        assertEquals(Json.encodeToString(expectedBody), response.bodyAsText())
    }
}