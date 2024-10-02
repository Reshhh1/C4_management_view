package com.example.team.business.service

import com.example.features.team.business.service.SearchTermParser
import com.example.features.user.business.model.UserSearchOptions
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class SearchTermParserTest {
    private val searchTermParser = SearchTermParser()

    @Test
    fun `parseUserSearchTerm should return a UserSearchOptions that separated the firstname prefixes and lastname`() {
        val searchTerm = "Job van Hakken"
        val actual = searchTermParser.parseUserSearchTerm(searchTerm)
        val expected = UserSearchOptions(
            "Job",
            "van",
            "Hakken"
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `parseUserSearchTerm should return a UserSearchOptions that separated the firstname and lastname`() {
        val searchTerm = "Mick Muis"
        val actual = searchTermParser.parseUserSearchTerm(searchTerm)
        val expected = UserSearchOptions(
            "Mick",
            null,
            "Muis"
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `parseUserSearchTerm should return a UserSearchOptions with only the firstname`() {
        val searchTerm = "Joep"
        val actual = searchTermParser.parseUserSearchTerm(searchTerm)
        val expected = UserSearchOptions(
            "Joep",
            null,
            null
        )
        assertEquals(expected, actual)
    }
}
