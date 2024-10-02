package com.example.session.business.model

import com.example.features.session.business.model.CredentialsValidator
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CredentialsValidatorTest {


    @Test
    fun `should return true if email is valid`() {
        val validEmail = "keesvandenkaas@hotmail.com"
        val validPassword = "Password1234$"
        val credentialsValidator =
               CredentialsValidator(validEmail, validPassword)
        assertTrue(credentialsValidator.validateEmail())
    }


    @Test
    fun `should return false if email has no @ sign`() {
        val validEmail = "keesvandenkaashotmail.com"
        val validPassword = "Password1234$"
        val credentialsValidator =
            CredentialsValidator(validEmail, validPassword)
        assertFalse(credentialsValidator.validateEmail())
    }


    @Test
    fun `should return false if email has no provider`() {
        val validEmail = "keesvandenkaas@hotmail"
        val validPassword = "Password1234$"
        val credentialsValidator =
            CredentialsValidator(validEmail, validPassword)
        assertFalse(credentialsValidator.validateEmail())
    }

    @Test
    fun `should return false if email has no domain name`() {
        val validEmail = "keesvandenkaas@.com"
        val validPassword = "Password1234$"
        val credentialsValidator =
            CredentialsValidator(validEmail, validPassword)
        assertFalse(credentialsValidator.validateEmail())
    }
}