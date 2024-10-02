package com.example.user.business.model

import com.example.features.user.business.model.*
import com.example.util.enums.*
import io.kotest.assertions.throwables.*
import io.ktor.server.plugins.*
import org.junit.*

class PasswordTest {

    @Test
    fun `checks all edge cases and should not throw an exception if password is valid`() {

        val passwordEdgeCases = listOf(
            "P@ssw0rd!",
            "StrongPassword123!@#$",
            "Abcd1234!",
            "Password$123",
            "MyP@ssword123",
            "LongP@ssword123456789!@#$%^&*()",
            "Mix3dP@ss",
            "P@ssword!!!123"
        )

        for (password in passwordEdgeCases) {
            val userBusiness =
                UserModel(1, "kees", "van den", "Kaas", Roles.MEMBER, "keeskaas@nl.abnamro.com", password)
            shouldNotThrow<BadRequestException> {
                userBusiness.validateUserInputs()
            }
        }
    }
}