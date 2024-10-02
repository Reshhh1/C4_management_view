package com.example.frontend_android.teams.presentation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.frontend_android.MainActivity
import com.example.frontend_android.ui.navigation.AppNavigation
import com.example.frontend_android.ui.navigation.AppScreens
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class TeamCreateScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
        composeRule.activity.setContent {
            AppNavigation(startDestination = AppScreens.TeamCreateScreen)
        }
    }

    /**
     * Performs a enter team name action
     * @author Reshwan Barhoe
     * @param teamName that's being entered
     */
    private fun enterTeamName(teamName: String) {
        composeRule.onNodeWithTag("input_team-name")
            .performTextInput(teamName)
    }


    /**
     * @author Reshwan Barhoe
     */
    @Test
    fun display_member_error_if_no_member_has_been_chosen() {
        enterTeamName("Valid team")
        composeRule.onNodeWithText("Add members").performClick()
        composeRule.onNodeWithContentDescription("Save").performClick()

        composeRule.onNodeWithText("At least 1 member required").assertExists()
    }

    /**
     * @author Reshwan Barhoe
     */
    @Test
    fun display_error_if_noting_filled_in() {
        composeRule.onNodeWithTag("input_team-name")
        composeRule.onNodeWithContentDescription("Save").performClick()

        composeRule.onNodeWithText("Must start with a letter").assertExists()
        composeRule.onNodeWithText("At least 1 member required").assertExists()
    }

    /**
     * @author Reshwan Barhoe
     */
    @Test
    fun display_name_error_if_team_name_does_not_start_with_letter() {
        enterTeamName("!Myteam")

        composeRule.onNodeWithText("Must start with a letter").assertExists()
    }

    /**
     * @author Reshwan Barhoe
     */
    @Test
    fun display_name_error_if_team_name_contains_invalid_character() {
        val input = "Bug&Bees"
        composeRule.onNodeWithTag("input_team-name")
            .performTextInput(input)

        composeRule.onNodeWithText("Can only contain -, _ , ! and spaces").assertExists()
    }

    /**
     * @author Reshwan Barhoe
     */
    @Test
    fun display_name_error_if_team_name_is_too_long() {
        val input =
            "a".repeat(64)
        composeRule.onNodeWithTag("input_team-name")
            .performTextInput(input)

        composeRule.onNodeWithText("Must be within 2 - 63 characters").assertExists()
    }

    /**
     * @author Reshwan Barhoe
     */
    @Test
    fun display_name_error_if_team_name_is_too_short() {
        val input = "a"
        composeRule.onNodeWithTag("input_team-name")
            .performTextInput(input)

        composeRule.onNodeWithText("Must be within 2 - 63 characters").assertExists()
    }
}