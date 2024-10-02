package com.example.frontend_android.teams.domain.model

import android.util.Log
import com.example.frontend_android.auth.domain.use_case.ValidationResult
import org.junit.Assert
import org.junit.Test

class TeamModelTest {

    /**
     * validateTeamName should return a successful ValidationResult
     * @author Reshwan Barhoe
     */
    @Test
    fun if_the_team_name_is_valid() {
        val validTeamName = "MyTeam"
        val teamModel = TeamModel(
            name = validTeamName,
            members = listOf(1, 2, 3)
        )
        val expected = ValidationResult(successful = true, "")
        val actual = teamModel.validateTeamName()
        Assert.assertEquals(expected, actual)
    }

    /**
     * validateTeamName should return a unsuccessful ValidationResult
     * @author Reshwan Barhoe
     */
    @Test
    fun if_the_team_name_contains_a_invalid_character() {
        val invalidTeamName = "MyTeam$"
        val teamModel = TeamModel(
            name = invalidTeamName,
            members = listOf(1, 2, 3)
        )
        val expected = ValidationResult(successful = false, "Can only contain -, _ , ! and spaces")
        val actual = teamModel.validateTeamName()
        Assert.assertEquals(expected, actual)
    }

    /**
     * validateTeamName should return a unsuccessful ValidationResult
     * @author Reshwan Barhoe
     */
    @Test
    fun if_the_team_name_does_not_contain_a_letter_as_first_character() {
        val invalidTeamName = "1MyTeam"
        val teamModel = TeamModel(
            name = invalidTeamName,
            members = listOf(1, 2, 3)
        )
        val expected = ValidationResult(successful = false, "Must start with a letter")
        val actual = teamModel.validateTeamName()
        Assert.assertEquals(expected, actual)
    }

    /**
     * validateTeamName should return a successful ValidationResult
     * @author Reshwan Barhoe
     */
    @Test
    fun if_the_team_name_is_the_min_length() {
        val validTeamName = "TT"
        val teamModel = TeamModel(
            name = validTeamName,
            members = listOf(1, 2, 3)
        )
        val expected = ValidationResult(successful = true, "")
        val actual = teamModel.validateTeamName()
        Assert.assertEquals(expected, actual)
    }

    /**
     * validateTeamName should return a unsuccessful ValidationResult
     * @author Reshwan Barhoe
     */
    @Test
    fun if_the_team_name_is_too_short() {
        val invalidTeamName = "T"
        val teamModel = TeamModel(
            name = invalidTeamName,
            members = listOf(1, 2, 3)
        )
        val expected = ValidationResult(successful = false, "Must be within 2 - 63 characters")
        val actual = teamModel.validateTeamName()
        Log.d("Hiero", expected.errorMessage)
        Log.d("Hiero", actual.errorMessage)

        Assert.assertEquals(expected, actual)
    }

    /**
     * validateTeamName should return a unsuccessful ValidationResult
     * @author Reshwan Barhoe
     */
    @Test
    fun if_the_team_name_is_the_max_length() {
        val validTeamName = "nkvmqererqfoadkxoakikvnyvoxjakufqqkobwyhoaoutrgegklobcgpxgwtlrh"
        val teamModel = TeamModel(
            name = validTeamName,
            members = listOf(1, 2, 3)
        )
        val expected = ValidationResult(successful = true, "")
        val actual = teamModel.validateTeamName()
        Assert.assertEquals(expected, actual)
    }

    /**
     * validateTeamName should return a unsuccessful ValidationResult
     * @author Reshwan Barhoe
     */
    @Test
    fun if_the_team_name_is_too_long() {
        val invalidTeamName = "jzrfhxggxwqmplmanwhtkohmbitvvampygmyovhsrlcvgwzyanrowdsqftkpbima"
        val teamModel = TeamModel(
            name = invalidTeamName,
            members = listOf(1, 2, 3)
        )
        val expected = ValidationResult(successful = false, "Must be within 2 - 63 characters")
        val actual = teamModel.validateTeamName()
        Assert.assertEquals(expected, actual)
    }

    /**
     * validateMembers should return a successful ValidationResult
     * @author Reshwan Barhoe
     */
    @Test
    fun if_the_team_members_are_valid() {
        val validMembers = listOf(1, 2, 3)
        val teamModel = TeamModel(
            name = "Team-1",
            members = validMembers
        )
        val expected = ValidationResult(successful = true, "")
        val actual = teamModel.validateMembers()
        Assert.assertEquals(expected, actual)
    }

    /**
     * validateMembers should return a unsuccessful ValidationResult
     * @author Reshwan Barhoe
     */
    @Test
    fun if_the_team_members_are_below_the_minimum_amount() {
        val invalidMembers = listOf<Int>()
        val teamModel = TeamModel(
            name = "Team-1",
            members = invalidMembers
        )
        val expected = ValidationResult(successful = false, "At least 1 member required")
        val actual = teamModel.validateMembers()
        Assert.assertEquals(expected, actual)
    }

    /**
     * validateMembers should return a successful ValidationResult
     * @author Reshwan Barhoe
     */
    @Test
    fun if_the_team_members_are_on_the_minimum_amount() {
        val validMembers = listOf(1)
        val teamModel = TeamModel(
            name = "Team-1",
            members = validMembers
        )
        val expected = ValidationResult(successful = true, "")
        val actual = teamModel.validateMembers()
        Assert.assertEquals(expected, actual)
    }

    /**
     * validateMembers should return a unsuccessful ValidationResult
     * @author Reshwan Barhoe
     */
    @Test
    fun if_the_team_members_are_above_the_maximum_amount() {
        val invalidMembers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
        val teamModel = TeamModel(
            name = "Team-1",
            members = invalidMembers
        )
        val expected = ValidationResult(successful = false, "Should have a maximum of 10 members")
        val actual = teamModel.validateMembers()
        Assert.assertEquals(expected, actual)
    }

    /**
     * validateMembers should return a successful ValidationResult
     * @author Reshwan Barhoe
     */
    @Test
    fun if_the_team_members_are_on_the_maximum_amount() {
        val validMembers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val teamModel = TeamModel(
            name = "Team-1",
            members = validMembers
        )
        val expected = ValidationResult(successful = true, "")
        val actual = teamModel.validateMembers()
        Assert.assertEquals(expected, actual)
    }

    /**
     * isMemberIdPresent should return true
     * @author Reshwan Barhoe
     */
    @Test
    fun if_the_provided_id_is_present() {
        val teamModel = TeamModel(
            name = "Team-1",
            members = listOf(1, 2, 3, 4)
        )
        val id = 3
        val expected = true
        val actual = teamModel.isMemberIdPresent(id)
        Assert.assertEquals(expected, actual)
    }

    /**
     * isMemberIdPresent should return false
     * @author Reshwan Barhoe
     */
    @Test
    fun if_the_provided_id_is_not_present() {
        val teamModel = TeamModel(
            name = "Team-1",
            members = listOf(1, 2, 3, 4)
        )
        val id = 10
        val expected = false
        val actual = teamModel.isMemberIdPresent(id)
        Assert.assertEquals(expected, actual)
    }
}