package com.example.user.business.model

import com.example.features.user.business.model.*
import com.example.util.enums.*
import io.kotest.assertions.throwables.*
import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import io.kotest.property.*
import io.kotest.property.arbitrary.*
import io.ktor.server.plugins.*

class UserModelTest : FunSpec({

    test("isFirstNameValid checks all edge cases and should return true") {
        checkAll(Arb.string(2..255)) { firstName ->
            val userBusiness =
                UserModel(1, firstName, "van den", "Kaas", Roles.MEMBER, "keeskaas@nl.abnamro.com", "Password1234$")
            userBusiness.validateUserInputs()
        }
    }


    test("isLastNameValid should return true") {
        checkAll(Arb.string(2..255)) { lastName ->
            val userBusiness =
                UserModel(1, "kees", "van den", lastName, Roles.MEMBER, "keeskaas@nl.abnamro.com", "Password1234$")
            userBusiness.validateUserInputs()
        }
    }

    test("isPrefixesValid should return true") {
        checkAll(Arb.string(0..20)) { prefixes ->
            val userBusiness =
                UserModel(1, "kees", prefixes, "Kaas", Roles.MEMBER, "keeskaas@nl.abnamro.com", "Password1234$")
            userBusiness.validateUserInputs()
        }
    }

    test("isEmailValid should return true") {
        val userBusinessForEmailRegex =
            UserModel(1, "kees", "van den", "Kaas", Roles.MEMBER, "keeskaas@nl.abnamro.com", "Password1234$")
        val emailRegex = userBusinessForEmailRegex.getEmailConfig()
        checkAll(Arb.stringPattern(emailRegex)) { email ->
            val userBusiness =
                UserModel(1, "kees", "van den", "Kaas", Roles.MEMBER, email, "Password1234$")
            userBusiness.validateUserInputs()
        }
    }

    test("isPasswordValid checks all edge cases and should throw BadRequestException") {
        val passwordEdgeCases = listOf(
            "Aa1!Bb2",
            "abcdefgh",
            "ABCDEFGH",
            "12345678",
            "!@#$%^&*",
            "AbCdEfGh",
            "Abc12345",
            "Abc!@#$%",
            "123!@#$%",
            "P@ssw0rd❤",
        )
        passwordEdgeCases.forEach { invalidPassword ->
            val userBusiness =
                UserModel(1, "kees", "van den", "Kaas", Roles.MEMBER, "keeskaas@nl.abnamro.com", invalidPassword)
            val exception = shouldThrow<BadRequestException> {
                userBusiness.validateUserInputs()
            }
            exception.message shouldBe ErrorCode.U_INVALID_PASSWORD.code
        }
    }

    test("isEmailValid checks all edge cases and should throw BadRequestException") {
        val invalidEmails = listOf(
            "kees_email@",
            "kees_email.com",
            "kees.email.com",
            "@email.com"
        )
        invalidEmails.forEach { invalidEmail ->
            val userBusiness =
                UserModel(1, "kees", "van den", "Kaas", Roles.MEMBER, invalidEmail, "Password1234$")
            val exception = shouldThrow<BadRequestException> {
                userBusiness.validateUserInputs()
            }
            exception.message shouldBe ErrorCode.U_INVALID_EMAIL.code
        }
    }

    test("isPrefixesValid checks all edge cases and should throw BadRequestException") {
        checkAll(Arb.string(21..255)) { prefixes ->
            val exception = shouldThrow<BadRequestException> {
                val userBusiness =
                    UserModel(1, "kees", prefixes, "Kaas", Roles.MEMBER, "keeskaas@nl.abnamro.com", "Password1234$")
                userBusiness.validateUserInputs()
            }
            exception.message shouldBe ErrorCode.U_INVALID_LENGTH_PREFIXES.code
        }
    }

    test("isLastName checks all edge cases and should throw BadRequestException") {
        checkAll(Arb.string(0..1)) { lastName ->
            val exception = shouldThrow<BadRequestException> {
                val userBusiness =
                    UserModel(1, "kees", "van den", lastName, Roles.MEMBER, "keeskaas@nl.abnamro.com", "Password1234$")
                userBusiness.validateUserInputs()
            }
            exception.message shouldBe ErrorCode.U_INVALID_LENGTH_LAST_NAME.code
        }
    }

    test("isFirstNameValid checks all edge cases and should throw BadRequestException") {
        checkAll(Arb.string(0..1)) { firstName ->
            val exception = shouldThrow<BadRequestException> {
                val userBusiness =
                    UserModel(1, firstName, "van den", "Kaas", Roles.MEMBER, "keeskaas@nl.abnamro.com", "Password1234$")
                userBusiness.validateUserInputs()
            }
            exception.message shouldBe ErrorCode.U_INVALID_LENGTH_FIRST_NAME.code
        }
    }
})