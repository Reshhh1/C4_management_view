package com.example.features.team.business.service

import com.example.features.user.business.model.UserSearchOptions

class SearchTermParser {

    /**
     * Parses the user search term into a UserSearchResult object.
     * The search term is being separated in different components so that
     * it could be used to turn it into a new object
     * @author Reshwan Barhoe
     * @param searchTerm that's being parsed
     * @return UserSearchResult object that could be used for searching
     */
    fun parseUserSearchTerm(searchTerm: String?): UserSearchOptions {
        val arrayOfSearchTerms = searchTerm?.trim()?.split(" ")
        return UserSearchOptions(
            firstName = arrayOfSearchTerms?.first(),
            prefixes = getPrefixesOrNull(arrayOfSearchTerms),
            lastName = getLastNameOrNull(arrayOfSearchTerms)
        )
    }

    /**
     * Checks if the arrayOfSearchTerms is bigger than 2, this indicates that the
     * array contains a firstname, prefixes, lastname
     * @author Reshwan Barhoe
     * @param arrayOfSearchTerms list of search terms
     * @return a string or null based on the size of the array
     */
    private fun getPrefixesOrNull(arrayOfSearchTerms: List<String>?): String? {
        return when {
            arrayOfSearchTerms?.size!! <= 2 -> null
            else -> arrayOfSearchTerms.subList(1, arrayOfSearchTerms.size - 1).joinToString(" ")
        }
    }

    /**
     * Checks if the arrayOfSearchTerms is smaller or equal to the size 1, this indicates that the
     * array contains only a firstname
     * @author Reshwan Barhoe
     * @param arrayOfSearchTerms list of search terms
     * @return a string or null based on the size of the array
     */
    private fun getLastNameOrNull(arrayOfSearchTerms: List<String>?): String? {
        return when {
            arrayOfSearchTerms?.size!! <= 1 -> null
            else -> arrayOfSearchTerms.last()
        }
    }
}
