package com.example.features.team.business.model

import com.example.util.enums.ErrorCode
import io.ktor.server.plugins.*
import java.util.*

/**
 * Team business model
 * @author Reshwan Barhoe
 */
data class TeamModel(
    val name: String,
    val members: List<Int>,
    val createdBy: Int,
    val createdAt: Date? = null
) {

    /**
     * Validates the team model
     * @author Reshwan barhoe
     */
    fun validateModel() {
        validateTeamName()
        validateMembers()
    }

    /**
     * Collection of validation checks for the teamName
     * @author Reshwan Barhoe
     */
    private fun validateTeamName() {
        checkTeamNameLength()
        checkFirstCharacter()
        checkForValidTeamNameCharacters()
    }

    /**
     * Collection of validation checks for the members
     * @author Reshwan Barhoe
     */
    private fun validateMembers() {
        checkMemberAmount()
        validateMemberIdUniqueness()
    }

    /**
     * Handles based on the given member amount value
     * @author Reshwan Barhoe
     * @throws BadRequestException if it exceeds above or below the min / max
     */
    private fun checkMemberAmount() {
        val minMembers = 1
        val maxMembers = 10
        if(members.isEmpty()) {
            throw BadRequestException(ErrorCode.T_MEMBER_IS_EMPTY.code)
        } else if (members.count() !in minMembers..maxMembers) {
            throw BadRequestException(ErrorCode.T_INVALID_MEMBER_AMOUNT.code)
        }
    }

    /**
     * Checks if the team name is with the min / max lengths
     * @author Reshwan Barhoe
     * @throws BadRequestException if the provided name isn't within the boundary
     */
    private fun checkTeamNameLength() {
        val minLength = 2
        val maxLength = 63
        if(name.isBlank()) {
            throw BadRequestException(ErrorCode.T_EMPTY_NAME.code)
        } else if (name.length !in minLength..maxLength) {
            throw BadRequestException(ErrorCode.T_INVALID_NAME_LENGTH.code)
        }
    }

    /**
     * Checks if the name starts with a letter
     * @author Reshwan Barhoe
     * @throws BadRequestException if the provided name doesn't start with a letter
     */
    private fun checkFirstCharacter() {
        if(!name[0].isLetter()) {
            throw BadRequestException(ErrorCode.T_INVALID_STARTING_CHARACTER.code)
        }
    }

    /**
     * Checks if the list of member ids contain duplicate ids
     * @author Reshwan Barhoe
     * @throws BadRequestException if the list of member ids contain duplicates
     */
    private fun validateMemberIdUniqueness() {
        val listOfUniqueIds = members.toHashSet()
        if(listOfUniqueIds.size != members.size) {
            throw BadRequestException(ErrorCode.T_DUPLICATE_MEMBER_IDS.code)
        }
    }

    /**
     * Checks if the name contains characters that aren't in
     * the acceptable regex
     * @author Reshwan Barhoe
     * @throws BadRequestException if the provided name contains a character that isn't accepted
     */
    private fun checkForValidTeamNameCharacters() {
        //Contains: numbers, -, _, ! and spaces
        val regexWithAcceptableCharacters = Regex("^[a-zA-Z0-9-_! ]+\$")
        regexWithAcceptableCharacters.find(name)
            ?: throw BadRequestException(ErrorCode.T_INVALID_NAME_CHARACTER.code)
    }
}