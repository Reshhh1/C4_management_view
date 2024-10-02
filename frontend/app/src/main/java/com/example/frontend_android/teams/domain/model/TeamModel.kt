package com.example.frontend_android.teams.domain.model

import com.example.frontend_android.auth.domain.use_case.ValidationResult
import kotlinx.serialization.Serializable

@Serializable
data class TeamModel(
    val name: String,
    var members: List<Int>
) {
    companion object {
        private const val maxMembers = 10
        private const val minMembers = 1
    }

    /**
     * Collection of validation checks for the teamName
     * @author Reshwan Barhoe
     * @return a ValidationResult object that indicates the result
     */
    fun validateTeamName(): ValidationResult {
        return when {
            !isFirstNameValid() -> ValidationResult(false, "Must start with a letter")
            !containsValidTeamNameCharacters() -> ValidationResult(
                false,
                "Can only contain -, _ , ! and spaces"
            )
            !isValidTeamNameLength() -> ValidationResult(false, "Must be within 2 - 63 characters")
            else -> ValidationResult(true, "")
        }
    }

    /**
     * Collection of validation checks for the team members
     * @author Reshwan Barhoe
     * @return the validation result
     */
    fun validateMembers(): ValidationResult {
        return when {
            isBelowMinimumMemberAmount() -> ValidationResult(
                false,
                "At least $minMembers member required"
            )

            isExceedingMaxMemberAmount() -> ValidationResult(
                false,
                "Should have a maximum of $maxMembers members"
            )

            else -> ValidationResult(true, "")
        }
    }

    /**
     * Collection of validation checks for the selected team members
     * @author Reshwan Barhoe
     * @return the validation result
     */
    fun validateSelectedMembers(): ValidationResult {
        return when {
            isBelowMinimumMemberAmount() -> ValidationResult(
                false,
                "Should at least have $minMembers member"
            )

            !isWithinMaxMemberBoundaries() -> ValidationResult(
                true,
                "Should have a maximum of $maxMembers members"
            )

            else -> ValidationResult(true, "")
        }
    }

    /**
     * Checks if the member amount exceeds the maximum
     * @author Reshwan Barhoe
     * @return if the max amount is exceeded
     */
    private fun isExceedingMaxMemberAmount(): Boolean {
        return members.size > maxMembers
    }

    /**
     * Checks if the member amount is within the max member boundaries
     * @author Reshwan Barhoe
     */
    fun isWithinMaxMemberBoundaries(): Boolean {
        return members.count() < maxMembers
    }

    /**
     * Checks if the provided id is present in the list of members
     * @author Reshwan Barhoe
     * @param id that's being checked
     * @return if the id is present
     */
    fun isMemberIdPresent(id: Int): Boolean {
        return members.contains(id)
    }

    /**
     * Checks if the member amount is below the minimum
     * @author Reshwan Barhoe
     * @return if the min member amount is below
     */
    private fun isBelowMinimumMemberAmount(): Boolean {
        return members.size < minMembers
    }

    /**
     * Checks if the team name is with the min / max lengths
     * @author Reshwan Barhoe
     * @return if the the name is within the required boundaries
     */
    private fun isValidTeamNameLength(): Boolean {
        val minLength = 2
        val maxLength = 63
        return (name.length in minLength..maxLength)
    }

    /**
     * Checks if the name starts with a letter
     * The not empty check is needed to ensure that there is value
     * to be checked
     * @author Reshwan Barhoe
     * @return if the first character is a letter
     */
    private fun isFirstNameValid(): Boolean {
        return name.isNotEmpty() && name[0].isLetter()
    }

    /**
     * Checks if the name contains characters that aren't in
     * the acceptable regex
     * @author Reshwan Barhoe
     * @return if the name contains valid characters
     */
    private fun containsValidTeamNameCharacters(): Boolean {
        //Contains: numbers, -, _, ! and spaces
        val regexWithAcceptableCharacters = Regex("^[a-zA-Z0-9-_! ]+\$")
        return regexWithAcceptableCharacters.matches(name)
    }
}