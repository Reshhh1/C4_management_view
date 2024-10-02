package com.example.frontend_android.users.domain.use_cases

import com.example.frontend_android.auth.domain.use_case.ValidationResult
import com.example.frontend_android.users.data.remote.dtos.UserSummary
import com.example.frontend_android.users.data.repository.UserRepository
import com.example.frontend_android.users.domain.model.UserSearch
import com.example.frontend_android.util.Resource
import javax.inject.Inject

class SearchUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Gets the validation results of the search term
     * @author Reshwan Barhoe
     * @param searchTerm that's being validated
     */
    fun getSearchTermValidationResults(searchTerm: String): ValidationResult {
        return UserSearch(searchTerm).validateSearchTerm()
    }

    /**
     * Gets the users by the provided search term by calling
     * the needed method in the data layer
     * @author Reshwan Barhoe
     * @param searchTerm that's being used for searching
     */
    suspend fun getUsersBySearchTerm(searchTerm: String): Resource<List<UserSummary>> {
        return userRepository.getUsersBySearchTerm(searchTerm)
    }
}