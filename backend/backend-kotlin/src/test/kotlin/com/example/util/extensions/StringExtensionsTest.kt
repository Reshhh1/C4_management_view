package com.example.util.extensions

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StringExtensionsTest {

    @Test
    fun `containsSpecialCharacters should return true if provided with a special character`() {
        val string = "!nice"
        assertTrue { string.containsSpecialCharacters() }
    }

    @Test
    fun `containsSpecialCharacters should return false if no special character is provided`() {
        val string = "justAString"
        assertFalse { string.containsSpecialCharacters() }
    }

    @Test
    fun `containsSpecialCharacters should return false even though a space is included`() {
        val string = "this time"
        assertFalse { string.containsSpecialCharacters() }
    }

    @Test
    fun `containsSpecialCharacters should return false if a special letter is provided`() {
        val string = "Özcan"
        assertFalse { string.containsSpecialCharacters() }
    }
}