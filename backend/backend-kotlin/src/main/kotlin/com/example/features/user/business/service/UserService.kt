package com.example.features.user.business.service

import com.example.features.team.business.service.*
import com.example.features.team.dtos.*
import com.example.features.user.data.repository.*
import com.example.features.user.dtos.*
import com.example.general.exception.*
import com.example.util.converters.*
import com.example.util.enums.*
import com.example.util.extensions.*
import io.ktor.server.plugins.*
import javax.inject.*


class UserService @Inject constructor(
    private val userRepository: UserRepository,
    private val searchTermParser: SearchTermParser,
    private val userFactory: UserFactory
) {

    /**
     * Gets the user details by their unique id.
     * @author Reshwan Barhoe
     * @param id The unique identifier of the user
     * @throws NotFoundException if the given userid doesn't exist
     * @return The user details of the user
     */
    suspend fun getUserDetailsById(id: Int): UserDetails {
        return userRepository.getUserDetailsById(id) ?: throw NotFoundException(ErrorCode.U_NOT_FOUND.code)
    }

    /**
     * Searches and retrieves a list of users by the provided searchOptions.
     * The searchTerm is being validated and parsed so that it could be used
     * correctly in the data layer
     * @author Reshwan Barhoe
     * @param searchOptions the provided searchOptions
     * @return the found users that match the search options
     */
    suspend fun getUsersBySearchOptions(searchOptions: SearchOptions): List<UserSummary> {
        validateSearchTerm(searchOptions.searchTerm)
        val userSearchOptions = searchTermParser.parseUserSearchTerm(searchOptions.searchTerm)
        return userRepository.getUsersBySearchOptions(userSearchOptions)
    }

    /**
     * Validates the searchTerm
     * @author Reshwan Barhoe
     * @param searchTerm that's being validated
     * @throws BadRequestException if the term contains a special character
     */
    private fun validateSearchTerm(searchTerm: String?) {
        if (searchTerm?.containsSpecialCharacters() == true) {
            throw BadRequestException(ErrorCode.G_INVALID_QUERY_CHARACTER.code)
        }
    }

    /**
     * registers the user
     * @author Ömer Aynaci
     * @param createUserDTO the user dto
     */
    suspend fun createUser(createUserDTO: CreateUserDTO) {
        val userModel = userFactory.convertToUserModel(createUserDTO)
        userModel.validateUserInputs()
        doesEmailExists(userModel.getEmail())
        userRepository.createUser(userModel)
    }

    /**
     * updates the user details
     * @author Ömer Aynaci
     * @param email the email of the user
     * @param updateUserDTO the update user dto
     */
    suspend fun updateUserDetails(email: String, updateUserDTO: UpdateUserDTO) {
        val userModel = userFactory.convertToUserModel(updateUserDTO)
        userModel.validateUpdateUser()
        val userId =
            userRepository.getUserIdByEmail(email) ?: throw NotFoundException(ErrorCode.U_NOT_FOUND.code)
        userRepository.updateUser(userId, userModel)
    }

    /**
     * Updates the password
     * @author Ömer Aynaci
     * @param email the email of the user
     * @param updatePasswordDTO the update password DTO
     */
    suspend fun updatePassword(email: String, updatePasswordDTO: UpdatePasswordDTO) {
        val userModel = userFactory.convertToUserModel(updatePasswordDTO)
        userModel.validatePassword()
        val existingPassword =
            userRepository.getPasswordByEmail(email) ?: throw throw NotFoundException(ErrorCode.U_NOT_FOUND.code)
        val isMatchingPassword = userModel.doesPasswordMatch(updatePasswordDTO.oldPassword, existingPassword)
        val userId = userRepository.getUserIdByEmail(email) ?: throw NotFoundException(ErrorCode.U_NOT_FOUND.code)
        isPasswordValid(userId, updatePasswordDTO, isMatchingPassword)
    }

    /**
     * Checks if the new password is not equal to the old password
     * @author Ömer Aynaci
     * @param userId the user id
     * @param updatePasswordDTO the update password DTO
     * @param isMatchingPassword verifies the old password that is hashed
     */
    private suspend fun isPasswordValid(
        userId: Int,
        updatePasswordDTO: UpdatePasswordDTO,
        isMatchingPassword: Boolean
    ) {
        if (updatePasswordDTO.newPassword != updatePasswordDTO.oldPassword) {
            if (isMatchingPassword) {
                userRepository.updatePassword(userId, updatePasswordDTO.newPassword)
            } else {
                throw UnauthorizedError(ErrorCode.U_INCORRECT_PASSWORD.code)
            }
        } else {
            throw BadRequestException(ErrorCode.U_NEW_PASSWORD_EQUAL_TO_OLD.code)
        }
    }

    /**
     * gets the first name of the user by email
     * @author Ömer Aynaci
     * @return the first name
     */
    suspend fun getFirstNameByEmail(email: String): String {
        return userRepository.getFirstNameByEmail(email)
    }

    /**
     * checks if the email exists or not
     * @author Ömer Aynaci
     * @throws EntityExistsException if the email already exists
     */
    private suspend fun doesEmailExists(email: String) {
        if (userRepository.getEmailIfExists(email) == email) {
            throw EntityExistsException(ErrorCode.U_EMAIL_EXISTS.code)
        }
    }
}