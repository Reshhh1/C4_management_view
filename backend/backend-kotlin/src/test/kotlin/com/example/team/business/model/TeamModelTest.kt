package com.example.team.business.model

import com.example.features.team.business.model.TeamModel
import com.example.util.enums.ErrorCode
import io.ktor.server.plugins.*
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import kotlin.test.assertFailsWith

class TeamModelTest {

    @Test
    @DisplayName("validateModel should throw noting")
    fun `if the teamModel is valid `() {
        val teamName = "BitesTheBug"
        val teamModel = TeamModel(teamName, listOf(1), createdBy = 1)
        assertDoesNotThrow {
            teamModel.validateModel()
        }
    }

    @Test
    @DisplayName("validateModel should throw a BadException with the required errorCode")
    fun `if the team name is 1 character below the required length `() {
        val teamName = "a"
        val teamModel = TeamModel(teamName, listOf(1), createdBy = 1)
        val exception = assertFailsWith<BadRequestException> {
            teamModel.validateModel()
        }
        assertEquals(ErrorCode.T_INVALID_NAME_LENGTH.code, exception.message)
    }

    @Test
    @DisplayName("validateModel should throw a BadException with the required errorCode")
    fun `if the team name is 1 character above the required length `() {
        val teamName = "a".repeat(64)
        val teamModel = TeamModel(teamName, listOf(1), createdBy = 1)
        val exception = assertFailsWith<BadRequestException> {
            teamModel.validateModel()
        }
        assertEquals(ErrorCode.T_INVALID_NAME_LENGTH.code, exception.message)
    }

    @Test
    @DisplayName("validateModel should throw noting")
    fun `if the team name is on the max required length `() {
        val teamName = "obhmcwicpouvxxcemgmnubhasvnfjyoqqwqzhohsuabhwqaxclqecttuaslezyj"
        val teamModel = TeamModel(teamName, listOf(1), createdBy = 1)
        assertDoesNotThrow {
            teamModel.validateModel()
        }
    }

    @Test
    @DisplayName("validateModel should throw noting")
    fun `if the team name is on the min required length `() {
        val teamName = "My"
        val teamModel = TeamModel(teamName, listOf(1), createdBy = 1)
        assertDoesNotThrow {
            teamModel.validateModel()
        }
    }

    @Test
    @DisplayName("validateModel should throw noting")
    fun `if the team name begins with a letter`() {
        val teamName = "B6team"
        val teamModel = TeamModel(teamName, listOf(1), createdBy = 1)
        assertDoesNotThrow {
            teamModel.validateModel()
        }
    }

    @Test
    @DisplayName("validateModel should throw BadException with the required errorCode")
    fun `if the team name doesn't begins with a letter`() {
        val teamName = "6team"
        val teamModel = TeamModel(teamName, listOf(1), createdBy = 1)
        val exception = assertFailsWith<BadRequestException> {
            teamModel.validateModel()
        }
        assertEquals(ErrorCode.T_INVALID_STARTING_CHARACTER.code, exception.message)
    }

    @Test
    @DisplayName("validateModel should throw noting")
    fun `if the team name contains a valid special character`() {
        val teamName = "B6-team"
        val teamModel = TeamModel(teamName, listOf(1), createdBy = 1)
        assertDoesNotThrow {
            teamModel.validateModel()
        }
    }

    @Test
    @DisplayName("validateModel should throw BadException with the required errorCode")
    fun `if the team name contains a invalid special character`() {
        val teamName = "B6@team"
        val teamModel = TeamModel(teamName, listOf(1), createdBy = 1)
        val exception = assertFailsWith<BadRequestException> {
            teamModel.validateModel()
        }
        assertEquals(ErrorCode.T_INVALID_NAME_CHARACTER.code, exception.message)
    }

    @Test
    @DisplayName("validateModel should throw BadException with the required errorCode")
    fun `if the team name is empty`() {
        val teamName = ""
        val teamModel = TeamModel(teamName, listOf(1), createdBy = 1)
        val exception = assertFailsWith<BadRequestException> {
            teamModel.validateModel()
        }
        assertEquals(ErrorCode.T_EMPTY_NAME.code, exception.message)
    }

    @Test
    @DisplayName("validateModel should throw noting")
    fun `if all the members have a valid id`() {
        val members = listOf(1,2,3)
        val teamModel = TeamModel("B6-team", members, createdBy = 1)
        assertDoesNotThrow {
            teamModel.validateModel()
        }
    }

    @Test
    @DisplayName("validateModel should throw noting")
    fun `if amount of members is on the maximum`() {
        val members = List(10) {it + 1}
        val teamModel = TeamModel("B6-team", members, createdBy = 1)
        assertDoesNotThrow {
            teamModel.validateModel()
        }
    }

    @Test
    @DisplayName("validateModel should throw noting")
    fun `if amount of members is on the minimum`() {
        val members = List(1) {it + 1}
        val teamModel = TeamModel("B6-team", members, createdBy = 1)
        assertDoesNotThrow {
            teamModel.validateModel()
        }
    }

    @Test
    @DisplayName("validateModel should throw BadException errorCode 33")
    fun `if the member amount is empty`() {
        val members = listOf<Int>()
        val teamModel = TeamModel("B6-team", members, createdBy = 1)
        val exception = assertFailsWith<BadRequestException> {
            teamModel.validateModel()
        }
        assertEquals(ErrorCode.T_MEMBER_IS_EMPTY.code, exception.message)
    }

    @Test
    @DisplayName("validateModel should throw BadException with the required errorCode")
    fun `if members contain duplicate ids`() {
        val members = listOf(1,2,2)
        val teamModel = TeamModel("B6-team", members, createdBy = 1)
        val exception = assertFailsWith<BadRequestException> {
            teamModel.validateModel()
        }
        assertEquals(ErrorCode.T_DUPLICATE_MEMBER_IDS.code, exception.message)
    }

    @Test
    @DisplayName("validateModel should throw BadException errorCode 33")
    fun `if amount of members is 1 above the maximum`() {
        val members = List(11) {it + 1}
        val teamModel = TeamModel("B6-team", members, createdBy = 1)
        val exception = assertFailsWith<BadRequestException> {
            teamModel.validateModel()
        }
        assertEquals(ErrorCode.T_INVALID_MEMBER_AMOUNT.code, exception.message)
    }
}