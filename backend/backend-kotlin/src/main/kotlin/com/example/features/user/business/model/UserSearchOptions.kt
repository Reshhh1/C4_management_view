package com.example.features.user.business.model

import org.jetbrains.exposed.sql.SortOrder

/**
 * Search results for the user.
 * Other features could be implemented besides sortBy
 * @author Reshwan Barhoe
 */
data class UserSearchOptions(
    val firstName: String?,
    val prefixes: String?,
    val lastName: String?,
    val sortBy: SortOrder = SortOrder.ASC,
    val maxSearchResults: Int = 100
)
