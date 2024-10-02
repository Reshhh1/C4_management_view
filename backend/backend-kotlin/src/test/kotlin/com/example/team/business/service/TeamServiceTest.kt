package com.example.team.business.service

import com.example.features.team.data.model.Team
import com.example.features.team.data.model.TeamUser
import com.example.features.team.dtos.CreateTeamDTO
import com.example.features.team.dtos.TeamSummary
import com.example.features.team.util.di.components.DaggerTeamHandlerComponent
import com.example.features.user.data.model.User
import com.example.general.exception.EntityExistsException
import com.example.team.helpers.TeamTestHelper
import com.example.util.DatabaseFactory
import io.ktor.server.plugins.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TeamServiceTest {
    @Mock
    val teamService = DaggerTeamHandlerComponent
        .create()
        .getTeamService()

    @BeforeTest
    fun setup() {
        testApplication {
            DatabaseFactory.init()
            transaction {
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

    @Test
    fun `createTeam should throw noting if the provided teamDTO is valid`() = runBlocking {
        testApplication {
            val createTeamDTO = CreateTeamDTO(
                "WinningTeam",
                listOf(2)
            )
            assertDoesNotThrow {
                teamService.createTeamAndReturnId(createTeamDTO, 1)
            }
        }
    }

    @Test
    fun `createTeamAndReturnId should return the team id if the team has been successfully created`() = runBlocking {
        testApplication {
            val createTeamDTO = CreateTeamDTO(
                "AntiCoders",
                listOf(2)
            )
            val actualId =  teamService.createTeamAndReturnId(createTeamDTO, 1)
            assertNotNull(actualId)
        }
    }

    @Test
    fun `createTeam should throw EntityExistsException if the team name already exists`() = runBlocking {
        testApplication {
            val createTeamDTO = CreateTeamDTO(
                "FireBeats",
                listOf(1,2,3)
            )
            assertThrows<EntityExistsException> {
                teamService.createTeamAndReturnId(createTeamDTO, 1)
            }
        }
    }

    @Test
    fun `createTeam should throw NotFound if the given member id doesn't exists`() = runBlocking {
        testApplication {
            val notExistingId = 130
            val createTeamDTO = CreateTeamDTO(
                "HeatFeat",
                listOf(1,2,notExistingId)
            )
            assertThrows<NotFoundException> {
                teamService.createTeamAndReturnId(createTeamDTO, 1)
            }
        }
    }

    @Test
    fun `getTeamsList should return a list of teams if the database has team data`() = runBlocking {
        testApplication {
            val teamList = teamService.getTeamSummaries()
            assertNotNull(teamList)
        }
    }

    @Test
    fun `getTeamsList should return a empty array if the data has no team data`() = runBlocking {
        testApplication {
            transaction { TeamUser.deleteAll() }
            transaction { Team.deleteAll() }
            val expected = emptyList<TeamSummary>()
            val teamList = teamService.getTeamSummaries()
            assertEquals(expected, teamList)
        }
    }

    @Test
    fun `getTeamDetailsById should return a list with team details if the provided id exists`() = runBlocking {
        testApplication {
            val result = teamService.getTeamDetailsById(1)
            assertNotNull(result)
        }
    }

    @Test
    fun `getTeamDetailsById should throw an exception if the provided id doesn't exists`() = runBlocking {
        testApplication {
           assertThrows<NotFoundException>{
               teamService.getTeamDetailsById(10000)
           }
        }
    }
}