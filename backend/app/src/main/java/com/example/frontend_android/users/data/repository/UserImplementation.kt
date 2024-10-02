package com.example.frontend_android.users.data.repository

import com.example.frontend_android.users.data.remote.UserApi
import com.example.frontend_android.users.data.remote.dtos.UserSummary
import com.example.frontend_android.users.data.remote.dtos.request.RegistrationRequest
import com.example.frontend_android.users.domain.model.RegistrationResult
import com.example.frontend_android.util.Constants
import com.example.frontend_android.util.Resource

class UserImplementation(
    private val userAPI: UserApi
) : UserRepository {

    /**
     * Makes use of the user API to get the users by the provided
     * search name.
     * @author Reshwan Barhoe
     * @param searchName that's being used in the request
     * @return A resource hierarchy
     */
    override suspend fun getUsersBySearchTerm(searchName: String): Resource<List<UserSummary>> {
        return try {
            val response = userAPI.getUserByName(searchName)
            if (response.isSuccessful) {
                response.body()?.let { responseBody ->
                    Resource.Success(responseBody)
                } ?: Resource.Error("No users")
            } else {
                Resource.Error(message = Constants.NETWORK_ERROR)
            }
        } catch (exception: Exception) {
            Resource.Error(Constants.NETWORK_ERROR)
        }
    }

    /**
     * creates a new user
     * @author Ömer Aynaci
     * @param newUser the registration object
     */
    override suspend fun createUser(newUser: RegistrationRequest): RegistrationResult<String> {
        val result: RegistrationResult<String> = try {
            val response = userAPI.createUser(newUser)
            if (response.isSuccessful) {
                RegistrationResult.Success("")
            } else {
                getRegistrationResult(response.code())
            }
        } catch (error: Exception) {
            RegistrationResult.UnknownError("Oops something went wrong, try again later", null)
        }
        return result
    }

    /**
     * gets the result of registration
     * @author Ömer Aynaci
     * @param statusCode the response status code
     * @return an instance of RegistrationValidation
     */
    private fun getRegistrationResult(statusCode: Int): RegistrationResult<String> {
        return when (statusCode) {
            409 -> RegistrationResult.EmailAlreadyExists("Email Already exists")
            else -> RegistrationResult.UnknownError("Oops something went wrong, try again later")
        }
    }
}