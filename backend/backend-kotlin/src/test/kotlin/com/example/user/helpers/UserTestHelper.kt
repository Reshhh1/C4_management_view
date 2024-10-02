package com.example.user.helpers

import com.example.features.user.data.entity.*
import com.example.features.user.dtos.*
import com.example.util.enums.*
import org.mindrot.jbcrypt.*

object UserTestHelper {
    private val listOfTestUsers = listOf(
        TestUser(1, "Henk", "van", "Jongen", "henkiscool@gmail.com", "password123!"),
        TestUser(2, "Pieter", "", "Brood"),
        TestUser(3, "Jannick", "van der", "Naaien"),
        TestUser(4, "Grietje", "den", "Bos"),
        TestUser(5, "Zelma", "", "Voorten"),
        TestUser(6, "Ömer", "", "Mokken"),
        TestUser(7, "Henk", "van", "Jongen", "henkvanjongen@nl.abnamro.com", "Password1234$")
    )

    const val TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvaGVsbG8iLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvIiwiZW1haWwiOiJoZW5rdmFuam9uZ2VuQG5sLmFibmFtcm8uY29tIiwiZXhwIjo5MjIzMzcyMDM2ODU0Nzc1fQ.c84B_m86D_EeXVnQWjl3DNgP_kRCSg0nczyKIOic1HY"

    /**
     * Gets all the test users, this list is getting converted and ordered
     * @author Reshwan Barhoe
     * @return list fo test UserSummaries
     */
    fun getAllTestUsers(): List<UserSummary> {
        return convertAndOrderToUserSummary(listOfTestUsers)
    }

    /**
     * Provides the data necessary for the searching users
     * @author Reshwan Barhoe
     */
    fun provideUserSearchData() {
        listOfTestUsers.map { insertUser(it) }
    }

    /**
     * Maps through the list of test users, converts and order the list
     * @author Reshwan Barhoe
     * @param listOfTestUser that's being converted and ordered
     * @return a list of UserSummary
     */
    fun convertAndOrderToUserSummary(listOfTestUser: List<TestUser>): List<UserSummary> {
        return listOfTestUser.map { convertToUserSummary(it) }
            .sortedBy { it.firstName }
    }

    /**
     * Converts the TestUser into a UserSummary
     * (This method can't be used in main code, I might make a factory converter for this later on)
     * @author Reshwan Barhoe
     * @param testUser object that's being converted
     * @return UserSummary
     */
    private fun convertToUserSummary(testUser: TestUser): UserSummary {
        return UserSummary(
            testUser.id,
            testUser.firstName,
            testUser.prefixes,
            testUser.lastName
        )
    }

    /**
     * Inserts a user into the database
     * @author Reshwan Barhoe
     * @param testUser that's being inserted
     */
    fun insertUser(testUser: TestUser) {
        UserEntity.new {
            firstName = testUser.firstName
            prefixes = testUser.prefixes
            lastName = testUser.lastName
            email = testUser.email
            password = BCrypt.hashpw(testUser.password, BCrypt.gensalt())
            role = Roles.MEMBER
        }
    }
}
