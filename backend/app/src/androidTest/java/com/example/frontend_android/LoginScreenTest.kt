package com.example.frontend_android

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class LoginScreenTest {

    /**
     * the test rule for hilt
     * @author Ömer Aynaci
     */
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    /**
     * the test rule for compose
     * @author Ömer Aynaci
     */
    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<MainActivity>()

    /**
     * Before the tests hilt injects the needed dependencies
     * @author Ömer Aynaci
     */
    @Before
    fun setup() {
        hiltRule.inject()
    }

    /**
     * Test case to verify that error messages are being displayed
     * when attempting to login with empty fields
     * @author Reshwan Barhoe
     */
    @Test
    fun login_with_noting_filled_in() {
        rule.onNodeWithText("Login").performClick()
        rule.onNodeWithText("The email can't be blank").assertExists()
        rule.onNodeWithText("The password can't be blank").assertExists()
    }

    /**
     * Test case to verify that error messages are being displayed
     * when attempting to login with a invalid email
     * @author Reshwan Barhoe
     */
    @Test
    fun login_with_incorrect_email() {
        rule.onNodeWithTag("EmailInput")
            .performTextInput("incorrect_email")
        rule.onNodeWithTag("PasswordInput")
            .performTextInput("password123")

        rule.onNodeWithText("Login").performClick()
        rule.onNodeWithText("Please enter a valid email").assertExists()
    }
}