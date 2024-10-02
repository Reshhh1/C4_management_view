package com.example.frontend_android.users.domain.model

import com.example.frontend_android.auth.domain.use_case.ValidationResult
import com.example.frontend_android.util.extensions.containsSpecialCharacters

data class UserSearch(
    val searchTerm: String
) {

    /**
     * Validates the search term
     * @author Reshwan Barhoe
     * @return the validation result
     */
    fun validateSearchTerm(): ValidationResult {
        return when {
            searchTerm.containsSpecialCharacters() -> ValidationResult(
                false,
                ""
            )

            else -> ValidationResult(true, "")
        }
    }
}