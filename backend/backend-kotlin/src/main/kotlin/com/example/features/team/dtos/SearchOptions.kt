package com.example.features.team.dtos

/**
 * Data class that's being used to store search options.
 * It's later possible to add other filter properties.
 * Filter / Search options are always optional so that's why it's nullable
 * @author Reshwan Barhoe
 */
data class SearchOptions(
    val searchTerm: String?
)