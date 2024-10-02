package com.example.features.user.data.repository

import com.example.features.user.business.model.*
import com.example.features.user.data.entity.*
import com.example.features.user.data.model.*
import com.example.features.user.dtos.*
import com.example.util.DatabaseFactory.dbQuery
import com.example.util.QueryBuilder
import com.example.util.converters.*
import com.example.util.enums.*
import org.jetbrains.exposed.sql.*
import org.mindrot.jbcrypt.*
import javax.inject.*

class UserRepository @Inject constructor(private val userFactory: UserFactory) {

    /**
     * Retrieves the user details by their id from the database
     *
     * @param id The unique identifier of the user
     * @return A function that queries the database and return the user details
     * or null
     * @author Reshwan Barhoe
     */
    suspend fun getUserDetailsById(id: Int): UserDetails? = dbQuery {
        return@dbQuery User.select { User.id eq id }
            .map { userFactory.convertToUserDetails(it) }.firstOrNull()
    }

    /**
     * Gets all the users that look similar like the provided user search options.
     * @author Reshwan Barhoe
     * @param userSearchOptions an object with the provided user search options
     * @return a list of UserSummary's
     */
    suspend fun getUsersBySearchOptions(userSearchOptions: UserSearchOptions): List<UserSummary> = dbQuery {
        val userSearchQuery = User.select {
            buildUserSearchConditions(userSearchOptions)
        }
        return@dbQuery performUserSearch(userSearchQuery, userSearchOptions)
    }

    /**
     * Performs the search query returns the processSearchResult, but if
     * the result is empty there will be searched for their firstname or lastname. This is seperated
     * to prevent the SQL operator from returning true each time a name contains the matching first or lastname
     * even though the full name doesn't completely match
     * @author Reshwan Barhoe
     * @param searchQuery query that's being applied
     * @param userSearchOptions to specify the criteria
     * @return a list of user summary
     */
    private fun performUserSearch(searchQuery: Query, userSearchOptions: UserSearchOptions): List<UserSummary> {
        val result = processSearchResults(searchQuery, userSearchOptions)
        return result.ifEmpty {
            val firstOrLastNameQuery = buildNameSearchQuery(userSearchOptions)
            processSearchResults(firstOrLastNameQuery, userSearchOptions)
        }
    }

    /**
     * Processes the search results by limiting, ordering and
     * converting it
     * @author Reshwan Barhoe
     * @param searchQuery that's being affected
     * @param userSearchOptions options for the search process
     * @return a list of user summary's
     */
    private fun processSearchResults(searchQuery: Query, userSearchOptions: UserSearchOptions): List<UserSummary> {
        return searchQuery.limit(userSearchOptions.maxSearchResults)
            .orderBy(
                column = User.firstName,
                order = userSearchOptions.sortBy
            ).map { userFactory.convertToUserSummary(it) }
    }

    /**
     * Builds a query for searching users based on their first/lastname, using the
     * provided search options
     * @author Reshwan Barhoe
     * @param userSearchOptions search options that are being provided
     * @return the name search query
     */
    private fun buildNameSearchQuery(userSearchOptions: UserSearchOptions): Query {
        return User.select {
            QueryBuilder.buildCiStartLikeCondition(User.firstName, userSearchOptions.firstName) or
                    QueryBuilder.buildCiStartLikeCondition(User.lastName, userSearchOptions.firstName)
        }
    }

    /**
     * Builds the user search query conditions,
     * Search options that could be used:
     * - Could search for the firstname prefixes lastname
     * - Could search for the prefixes lastname
     * - Could search for firstname prefixes
     * - Could search for firstname lastname
     * @author Reshwan Barhoe
     * @param userSearchOptions that forms the condition
     * @return a boolean operation that's being used for the query statements
     */
    private fun buildUserSearchConditions(
        userSearchOptions: UserSearchOptions
    ): Op<Boolean> {
        val firstNameCondition = QueryBuilder.buildCiStartLikeCondition(User.firstName, userSearchOptions.firstName)
        val prefixesCondition = QueryBuilder.buildCiStartLikeCondition(User.prefixes, userSearchOptions.prefixes)
        val lastNameCondition = QueryBuilder.buildCiStartLikeCondition(User.lastName, userSearchOptions.lastName)

        return (QueryBuilder.buildCiExactLikeCondition(
            User.firstName,
            userSearchOptions.firstName
        ) and lastNameCondition) or
                (firstNameCondition and QueryBuilder.buildCiStartLikeCondition(
                    User.prefixes,
                    userSearchOptions.lastName
                )) or
                (firstNameCondition and prefixesCondition and lastNameCondition) or
                (QueryBuilder.buildCiStartLikeCondition(
                    User.prefixes,
                    userSearchOptions.firstName
                ) and lastNameCondition) or
                (QueryBuilder.buildCiExactLikeCondition(
                    User.firstName,
                    userSearchOptions.firstName
                ) and lastNameCondition) or
                (firstNameCondition and QueryBuilder.buildCiStartLikeCondition(
                    User.prefixes,
                    userSearchOptions.lastName
                ))
    }

    /**
     * Gets the email
     * @author Ömer Aynaci
     * @param email the email of the user
     * @return true if the email exists otherwise false
     */
    suspend fun getEmail(email: String?): Boolean = dbQuery {
        if (email != null) {
            val result = User.select { User.email eq email }.singleOrNull()
            return@dbQuery result != null
        } else {
            return@dbQuery false
        }
    }

    /**
     * gets the first name by email
     * @author Ömer Aynaci
     * @param email the user's email
     * @return the first name of the user
     */
    suspend fun getFirstNameByEmail(email: String): String = dbQuery {
        val result = User.select { User.email eq email }.singleOrNull()
        result?.let {
            return@dbQuery result[User.firstName]
        }
        ""
    }

    /**
     * gets the user id by email
     * @author Ömer Aynaci
     * @param email the user's email
     * @return the user id
     */
    suspend fun getUserIdByEmail(email: String): Int? = dbQuery {
        val result = User.select { User.email eq email }.singleOrNull()
        return@dbQuery (result?.get(User.id)?.value)
    }

    /**
     * gets the email of the user if it exists
     * @author Ömer Aynaci
     * @param email the email that exists or not
     * @return the email
     */
    suspend fun getEmailIfExists(email: String): String? = dbQuery {
        val result = User.select { User.email eq email }.singleOrNull()
        return@dbQuery (result?.get(User.email))
    }

    /**
     * gets the password
     * @author Ömer Aynaci
     * @param email the email of the user
     * @return the password
     */
    suspend fun getPasswordByEmail(email: String): String? = dbQuery {
        val existingUser = User.select { User.email eq email }
            .map { userFactory.convertToBusinessModel(it) }
            .singleOrNull()
        return@dbQuery existingUser?.getHashedPassword()
    }

    /**
     * gets the email of the user
     * @author Ömer Aynaci
     * @param email the email of the user
     * @return the user model
     */
    suspend fun getUserByEmail(email: String): UserModel? = dbQuery {
        return@dbQuery User.select { User.email eq email }
            .map { userFactory.convertToBusinessModel(it) }
            .singleOrNull()
    }

    /**
     * Gets the UserEntity based on the provided id
     * @author Reshwan Barhoe
     * @param id of the user that's being searched for
     * @return [UserEntity]
     */
    suspend fun getUserEntityById(id: Int): UserEntity? = dbQuery {
        return@dbQuery UserEntity.findById(id)
    }


    /**
     * creates a user
     * @author Ömer Aynaci
     * @param user the user data model
     */
    suspend fun createUser(user: UserModel) = dbQuery {
        User.insert {
            it[firstName] = user.getFirstName()
            it[lastName] = user.getLastName()
            it[prefixes] = user.getPrefixes()!!
            it[role] = Roles.MEMBER
            it[email] = user.getEmail()
            it[password] = user.hashPassword()
        }
    }

    /**
     * updates the user details
     * @author Ömer Aynaci
     * @param userId the id of the user
     * @param user the user data model
     */
    suspend fun updateUser(userId: Int, user: UserModel): Int = dbQuery {
        User.update({ User.id eq userId }) {
            it[firstName] = user.getFirstName()
            it[lastName] = user.getLastName()
            it[prefixes] = user.getPrefixes()!!
            it[email] = user.getEmail()
            it[role] = user.getRole()
        }
    }

    /**
     * updates the password
     * @author Ömer Aynaci
     * @param userId the id of the user
     * @param password password that's being hashed
     */
    suspend fun updatePassword(userId: Int, password: String): Int = dbQuery {
        User.update({ User.id eq userId }) {
            it[this.password] = BCrypt.hashpw(password, BCrypt.gensalt())
        }
    }
}