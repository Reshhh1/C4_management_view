package com.example.user.business.service

import com.example.features.team.dtos.*
import com.example.features.user.data.model.*
import com.example.features.user.dtos.*
import com.example.features.user.util.di.components.*
import com.example.general.exception.*
import com.example.user.helpers.*
import com.example.util.enums.*
import com.typesafe.config.*
import io.ktor.server.plugins.*
import io.ktor.server.testing.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.junit.Test
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.*
import kotlin.test.*


class UserServiceTest {
    @Mock
    private val userService = DaggerUserHandlerComponent
        .create()
        .getUserService()

    private val config = ConfigFactory.load("testApplication.conf")

    private val database = Database.connect(
        url = config.getString("ktor.database.url"),
        driver = config.getString("ktor.database.driver"),
        user = config.getString("ktor.database.user"),
        password = config.getString("ktor.database.password")
    )

    @BeforeTest
    fun setup() {
        testApplication {
            transaction(database) {
                SchemaUtils.create(User)
                UserTestHelper.provideUserSearchData()
            }

        }
    }

    @AfterTest
    fun cleanup() {
        runBlocking {
            transaction(database) {
                SchemaUtils.drop(User)
                rollback()
            }
        }
    }

    @Test
    fun `getUserInformationById should return a UserPreviewDTO if the user is found`() = runBlocking {
        testApplication {
            val id = 1
            val expected = UserDetails(
                "Henk",
                "van",
                "Jongen",
                "henkiscool@gmail.com"
            )
            val actual = userService.getUserDetailsById(id)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `getUserInformationById should throw a NotFoundException if the user is not found`() = runBlocking {
        testApplication {
            val nonExistingId = 10
            assertThrows<NotFoundException> {
                userService.getUserDetailsById(nonExistingId)
            }
        }
    }

    @Test
    fun `getUsersBySearchOptions should return a list with users that start with the B`() = runBlocking {
        testApplication {
            val searchTerm = "B"
            val expected = UserTestHelper.convertAndOrderToUserSummary(listOf(
                TestUser(4, "Grietje", "den", "Bos"),
                TestUser(2, "Pieter", "", "Brood")
            ))
            val actual = userService.getUsersBySearchOptions(SearchOptions(searchTerm))
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `getUsersBySearchOptions should return a list with users that have the same prefix and lastname`() = runBlocking {
        testApplication {
            val searchTerm = "van der Naaien"
            val expected = UserTestHelper.convertAndOrderToUserSummary(listOf(
                TestUser(3, "Jannick", "van der", "Naaien")
            ))
            val actual = userService.getUsersBySearchOptions(SearchOptions(searchTerm))
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `getUsersBySearchOptions should return a list with users that have the same firstname`() = runBlocking {
        testApplication {
            val searchTerm = "Henk"
            val expected = UserTestHelper.convertAndOrderToUserSummary(listOf(
                TestUser(1, "Henk", "van", "Jongen"),
                TestUser(7,"Henk","van","Jongen")
            ))
            val actual = userService.getUsersBySearchOptions(SearchOptions(searchTerm))
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `getUsersBySearchOptions should return a list with users that have the same firstname and lastname`() = runBlocking {
        testApplication {
            val searchTerm = "Zelma Voorten"
            val expected = UserTestHelper.convertAndOrderToUserSummary(listOf(
                TestUser(5, "Zelma", "", "Voorten")
            ))
            val actual = userService.getUsersBySearchOptions(SearchOptions(searchTerm))
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `getUsersBySearchOptions should return a list with users that have a special letter`() = runBlocking {
        testApplication {
            val searchTerm = "Ömer Mokken"
            val expected = UserTestHelper.convertAndOrderToUserSummary(listOf(
                TestUser(6, "Ömer", "", "Mokken")
            ))
            val actual = userService.getUsersBySearchOptions(SearchOptions(searchTerm))
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `getUsersBySearchOptions should return a empty array if there is no data found`() = runBlocking {
        testApplication {
            val searchTerm = "notfound"
            val actual = userService.getUsersBySearchOptions(SearchOptions(searchTerm))
            assertEquals(emptyList<UserSummary>(), actual)
        }
    }

    @Test
    fun `getUsersBySearchOptions should throw BadException if the searchTerm contains a special character`() = runBlocking {
        testApplication {
            val searchTerm = "Gr!"
            assertThrows<BadRequestException> {
                userService.getUsersBySearchOptions(SearchOptions(searchTerm))
            }
        }
    }

    @Test
    fun `getUsersBySearchOptions should return a list with users in alphabetical order if no searchTerm is given`() = runBlocking {
        testApplication {
            val searchTerm = ""
            val actual = userService.getUsersBySearchOptions(SearchOptions(searchTerm))
            assertEquals(UserTestHelper.getAllTestUsers(), actual)
        }
    }


    @Test
    fun `registerUser should throw BadRequestException if first name has less than 2 characters`() = testApplication {
        val newUser = CreateUserDTO("K", "Kaas", "van den","keeskaas@nl.abnamro.com", "Password1234$")
        assertThrows<BadRequestException> {
            userService.createUser(newUser)
        }
    }

    @Test
    fun `registerUser should throw BadRequestException if first name has more than 255 characters`() = testApplication {
        val firstNameLimitExceeded = "Kees".repeat(260)
        val newUser = CreateUserDTO(firstNameLimitExceeded, "Kaas", "van den","keeskaas@nl.abnamro.com", "Password1234$")
        assertThrows<BadRequestException> {
            userService.createUser(newUser)
        }
    }

    @Test
    fun `registerUser should create a new user`() = testApplication {
        val newUser = CreateUserDTO("Kees", "Kaas", "van den","keeskaas@nl.abnamro.com", "Password1234$")
        assertDoesNotThrow {
            runBlocking {
                userService.createUser(newUser)
            }
        }
    }

    @Test
    fun `updateUserDetails should update user`() = testApplication {
        val user = UpdateUserDTO("henk","jongen", "","henkvanjongen@nl.abnamro.com",Roles.MANAGER)
        val email = user.email
        assertDoesNotThrow {
            runBlocking {
                userService.updateUserDetails(email,user)
            }
        }
    }

    @Test
    fun `updateUserDetails should throw BadRequestException if first name has less than 2 characters`() = testApplication {
        val user = UpdateUserDTO("h","jongen", "","henkvanjongen@nl.abnamro.com",Roles.MANAGER)
        val email = user.email
        assertThrows<BadRequestException> {
            userService.updateUserDetails(email,user)
        }
    }

    @Test
    fun `updateUserDetails should throw BadRequestException if first name has more than 255 characters`() = testApplication {
        val invalidFirstName = "Kees".repeat(256)
        val user = UpdateUserDTO(invalidFirstName,"jongen", "","henkvanjongen@nl.abnamro.com",Roles.MANAGER)
        val email = user.email
        assertThrows<BadRequestException> {
            userService.updateUserDetails(email,user)
        }
    }

    @Test
    fun `updateUserDetails should throw BadRequestException if last name has more than 255 characters`() = testApplication {
        val invalidLastName = "jongen".repeat(256)
        val user = UpdateUserDTO("Kees",invalidLastName, "","henkvanjongen@nl.abnamro.com",Roles.MANAGER)
        val email = user.email
        assertThrows<BadRequestException> {
            userService.updateUserDetails(email,user)
        }
    }

    @Test
    fun `updateUserDetails should throw BadRequestException if last name has less than 2 characters`() = testApplication {
        val user = UpdateUserDTO("Kees","j", "","henkvanjongen@nl.abnamro.com",Roles.MANAGER)
        val email = user.email
        assertThrows<BadRequestException> {
            userService.updateUserDetails(email,user)
        }
    }

    @Test
    fun `updatePassword should update password`() = testApplication {
        val password = UpdatePasswordDTO("password123!", "Voetbal11@")
        val email = "henkiscool@gmail.com"
        assertDoesNotThrow {
            runBlocking {
                userService.updatePassword(email,password)
            }
        }
    }

    @Test
    fun `updatePassword should throw BadRequestException if old password doesn't match the stored password`() = testApplication {
        val password = UpdatePasswordDTO("password123!sssssss", "Voetbal1112@")
        val email = "henkiscool@gmail.com"
        assertThrows<UnauthorizedError> {
            userService.updatePassword(email,password)
        }
    }

    @Test
    fun `updatePassword should throw BadRequestException if new password is equal to old password`() = testApplication {
        val password = UpdatePasswordDTO("password123!", "password123!")
        val email = "henkiscool@gmail.com"
        assertThrows<BadRequestException> {
            userService.updatePassword(email,password)
        }
    }

    @Test
    fun `updatePassword should throw BadRequestException if new password is invalid`() = testApplication {
        val password = UpdatePasswordDTO("password123!", "password")
        val email = "henkiscool@gmail.com"
        assertThrows<BadRequestException> {
            userService.updatePassword(email,password)
        }
    }

}
