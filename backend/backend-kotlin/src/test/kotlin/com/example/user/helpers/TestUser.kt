package com.example.user.helpers

import com.example.util.enums.Roles

/**
 * Test model that's being used for creating
 * test entities
 * @author Reshwan Barhoe
 */
data class TestUser(
    val id: Int,
    val firstName: String,
    val prefixes: String,
    val lastName: String,
    val email: String = "",
    val password: String = "",
    val role: Roles = Roles.MEMBER
)