package com.example.frontend_android

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.frontend_android.ui.components.wrappers.ApplicationWrapper
import com.example.frontend_android.ui.navigation.AppNavigation
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class RegistrationScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
        composeRule.activity.setContent {
            ApplicationWrapper {
                AppNavigation()
            }
        }
    }


    /**
     * Test scenario if first name has less than 2 characters
     * @author Ömer Aynaci
     */
    @Test
    fun validateFirstNameLength_showsErrorMessage_ifTooShort() {
        composeRule.onNodeWithText("I don't have an account").performClick()
        composeRule.onNodeWithTag("FirstName").performTextInput("s")
        composeRule.onNodeWithText("First name must be at least 2 characters long").assertExists()
    }

    /**
     * Test scenario if first name has more than 255 characters
     * @author Ömer Aynaci
     */
    @Test
    fun validateFirstNameLength_showsErrorMessage_ifTooLong() {
        val invalidFirstName = "kees".repeat(256)
        composeRule.onNodeWithText("I don't have an account").performClick()
        composeRule.onNodeWithTag("FirstName").performTextInput(invalidFirstName)
        composeRule.onNodeWithText("First name cannot be longer than 255 characters").assertExists()
    }

    /**
     * Test scenario if prefixes has exceeded 20 characters
     * @author Ömer Aynaci
     */
    @Test
    fun validatePrefixesLength_showsErrorMessage_ifTooLong() {
        val invalidPrefixes = "van".repeat(30)
        composeRule.onNodeWithText("I don't have an account").performClick()
        composeRule.onNodeWithTag("Prefixes").performTextInput(invalidPrefixes)
        composeRule.onNodeWithText("Prefixes cannot exceed 20 characters").assertExists()
    }


    /**
     * Test scenario if email has no provider
     * @author Ömer Aynaci
     */
    @Test
    fun validateEmail_showsErrorMessage_if_email_has_no_provider() {
        val invalidEmail = "keeskaas@."
        composeRule.onNodeWithText("I don't have an account").performClick()
        composeRule.onNodeWithTag("FirstName").performTextInput("kees")

        composeRule.onNodeWithTag("Prefixes").performTextInput("van den")

        composeRule.onNodeWithTag("LastName").performTextInput("Kaas")

        composeRule.onNodeWithText("Next").performClick()

        composeRule.onNodeWithTag("EmailAddress").performTextInput(invalidEmail)

        composeRule.onNodeWithText("Please enter a valid email address").assertExists()
    }

    /**
     * Test scenario if email has no domain
     * @author Ömer Aynaci
     */
    @Test
    fun validateEmail_showsErrorMessage_if_email_has_no_domain() {
        val invalidEmail = "keeskaas@hotmail"
        composeRule.onNodeWithText("I don't have an account").performClick()
        composeRule.onNodeWithTag("FirstName").performTextInput("kees")

        composeRule.onNodeWithTag("Prefixes").performTextInput("van den")

        composeRule.onNodeWithTag("LastName").performTextInput("Kaas")

        composeRule.onNodeWithText("Next").performClick()

        composeRule.onNodeWithTag("EmailAddress").performTextInput(invalidEmail)

        composeRule.onNodeWithText("Please enter a valid email address").assertExists()
    }

    /**
     * Test scenario if email has no @ sign
     * @author Ömer Aynaci
     */
    @Test
    fun validateEmail_showsErrorMessage_if_email_has_at_sign() {
        val invalidEmail = "keeskaashotmail.com"
        composeRule.onNodeWithText("I don't have an account").performClick()
        composeRule.onNodeWithTag("FirstName").performTextInput("kees")

        composeRule.onNodeWithTag("Prefixes").performTextInput("van den")

        composeRule.onNodeWithTag("LastName").performTextInput("Kaas")

        composeRule.onNodeWithText("Next").performClick()

        composeRule.onNodeWithTag("EmailAddress").performTextInput(invalidEmail)

        composeRule.onNodeWithText("Please enter a valid email address").assertExists()
    }

    /**
     * Test scenario for if email already exists
     * @author Ömer Aynaci
     */
    @Test
    fun validateEmail_showsErrorMessage_if_email_already_exists() {
        composeRule.onNodeWithText("I don't have an account").performClick()

        composeRule.onNodeWithTag("FirstName").performTextInput("kees")

        composeRule.onNodeWithTag("Prefixes").performTextInput("van den")

        composeRule.onNodeWithTag("LastName").performTextInput("Kaas")

        composeRule.onNodeWithText("Next").performClick()

        composeRule.onNodeWithTag("EmailAddress").performTextInput("keesvandenbos@nl.abnamro.nl")

        composeRule.onNodeWithText("Next").performClick()

        composeRule.onNodeWithTag("Password").performTextInput("Password1234$")

        composeRule.onNodeWithTag("ConfirmPassword").performTextInput("Password1234$")

        composeRule.onNodeWithText("Register").performClick()

        composeRule.onNodeWithText("Email Already exists").assertExists()
    }


    /**
     * Test scenario if password has less than 8 characters
     * @author Ömer Aynaci
     */
    @Test
    fun validatePasswordLength_showErrorMessage_if_password_has_less_than_8_characters() {
        composeRule.onNodeWithText("I don't have an account").performClick()

        composeRule.onNodeWithTag("FirstName").performTextInput("kees")

        composeRule.onNodeWithTag("Prefixes").performTextInput("van den")

        composeRule.onNodeWithTag("LastName").performTextInput("Kaas")

        composeRule.onNodeWithText("Next").performClick()

        composeRule.onNodeWithTag("EmailAddress").performTextInput("keesvandenkaas@nl.abnamro.nl")

        composeRule.onNodeWithText("Next").performClick()

        composeRule.onNodeWithTag("Password").performTextInput("Pas")

        composeRule.onNodeWithText("Password should have at least 8 characters").assertExists()

        composeRule.onNodeWithTag("ConfirmPassword").performTextInput("Password1234$")
    }

    /**
     * Test scenario if password has no lowercase characters
     * @author Ömer Aynaci
     */
    @Test
    fun validatePasswordLowerCase_showErrorMessage_if_password_has_no_lowercase_characters() {
        composeRule.onNodeWithText("I don't have an account").performClick()

        composeRule.onNodeWithTag("FirstName").performTextInput("kees")

        composeRule.onNodeWithTag("Prefixes").performTextInput("van den")

        composeRule.onNodeWithTag("LastName").performTextInput("Kaas")

        composeRule.onNodeWithText("Next").performClick()

        composeRule.onNodeWithTag("EmailAddress").performTextInput("keesvandenkaas@nl.abnamro.nl")

        composeRule.onNodeWithText("Next").performClick()

        composeRule.onNodeWithTag("Password").performTextInput("PASSWORD1234$")

        composeRule.onNodeWithText("Password should contain at least 1 lowercase").assertExists()

        composeRule.onNodeWithTag("ConfirmPassword").performTextInput("Password1234$")
    }


    /**
     * Test scenario if password has no uppercase characters
     * @author Ömer Aynaci
     */
    @Test
    fun validatePasswordUppercase_showErrorMessage_if_password_has_no_uppercase_characters() {
        composeRule.onNodeWithText("I don't have an account").performClick()

        composeRule.onNodeWithTag("FirstName").performTextInput("kees")

        composeRule.onNodeWithTag("Prefixes").performTextInput("van den")

        composeRule.onNodeWithTag("LastName").performTextInput("Kaas")

        composeRule.onNodeWithText("Next").performClick()

        composeRule.onNodeWithTag("EmailAddress").performTextInput("keesvandenkaas@nl.abnamro.nl")

        composeRule.onNodeWithText("Next").performClick()

        composeRule.onNodeWithTag("Password").performTextInput("password1234$")

        composeRule.onNodeWithText("Password should contain at least 1 uppercase").assertExists()

        composeRule.onNodeWithTag("ConfirmPassword").performTextInput("Password1234$")
    }

    /**
     * Test scenario if password has no special characters
     * @author Ömer Aynaci
     */
    @Test
    fun validatePasswordSpecialCharacter_showErrorMessage_if_password_has_no_special_characters() {
        composeRule.onNodeWithText("I don't have an account").performClick()

        composeRule.onNodeWithTag("FirstName").performTextInput("kees")

        composeRule.onNodeWithTag("Prefixes").performTextInput("van den")

        composeRule.onNodeWithTag("LastName").performTextInput("Kaas")

        composeRule.onNodeWithText("Next").performClick()

        composeRule.onNodeWithTag("EmailAddress").performTextInput("keesvandenkaas@nl.abnamro.nl")

        composeRule.onNodeWithText("Next").performClick()

        composeRule.onNodeWithTag("Password").performTextInput("Password1234")

        composeRule.onNodeWithText("Password should contain at least 1 special character")
            .assertExists()

        composeRule.onNodeWithTag("ConfirmPassword").performTextInput("Password1234$")
    }


    /**
     * Test scenario if password has no numbers
     * @author Ömer Aynaci
     */
    @Test
    fun validatePasswordNumber_showErrorMessage_if_password_has_no_numbers() {
        composeRule.onNodeWithText("I don't have an account").performClick()

        composeRule.onNodeWithTag("FirstName").performTextInput("kees")

        composeRule.onNodeWithTag("Prefixes").performTextInput("van den")

        composeRule.onNodeWithTag("LastName").performTextInput("Kaas")

        composeRule.onNodeWithText("Next").performClick()

        composeRule.onNodeWithTag("EmailAddress").performTextInput("keesvandenkaas@nl.abnamro.nl")

        composeRule.onNodeWithText("Next").performClick()

        composeRule.onNodeWithTag("Password").performTextInput("Password$$$")

        composeRule.onNodeWithText("Password should contain at least 1 number").assertExists()

        composeRule.onNodeWithTag("ConfirmPassword").performTextInput("Password1234$")
    }

    /**
     * Test scenario if confirm password doesn't match the password
     * @author Ömer Aynaci
     */
    @Test
    fun validateConfirmPassword_showErrorMessage_if_confirmPassword_doesnt_match_thePassword() {
        composeRule.onNodeWithText("I don't have an account").performClick()

        composeRule.onNodeWithTag("FirstName").performTextInput("kees")

        composeRule.onNodeWithTag("Prefixes").performTextInput("van den")

        composeRule.onNodeWithTag("LastName").performTextInput("Kaas")

        composeRule.onNodeWithText("Next").performClick()

        composeRule.onNodeWithTag("EmailAddress").performTextInput("keesvandenkaas@nl.abnamro.nl")

        composeRule.onNodeWithText("Next").performClick()

        composeRule.onNodeWithTag("Password").performTextInput("Password123455%")

        composeRule.onNodeWithTag("ConfirmPassword").performTextInput("Password1234$")

        composeRule.onNodeWithText("Password doesn't match").assertExists()
    }

    /**
     * Test scenario for when user registers successfully
     * @author Ömer Aynaci
     */
    @Test
    fun register_successful() {
        val email = EmailMonkeyTest().generateRandomEmail()
        composeRule.onNodeWithText("I don't have an account").performClick()

        composeRule.onNodeWithTag("FirstName").performTextInput("kees")

        composeRule.onNodeWithTag("Prefixes").performTextInput("van den")

        composeRule.onNodeWithTag("LastName").performTextInput("Kaas")

        composeRule.onNodeWithText("Next").performClick()

        composeRule.onNodeWithTag("EmailAddress").performTextInput(email)

        composeRule.onNodeWithText("Next").performClick()

        composeRule.onNodeWithTag("Password").performTextInput("Password1234$")

        composeRule.onNodeWithTag("ConfirmPassword").performTextInput("Password1234$")

        composeRule.onNodeWithText("Register").performClick()
    }
}