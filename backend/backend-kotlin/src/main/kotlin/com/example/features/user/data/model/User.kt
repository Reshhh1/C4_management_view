package com.example.features.user.data.model

import com.example.util.enums.*
import org.jetbrains.exposed.dao.id.*

/**
 * The User table of the database.
 * Stores basic information about the user
 * @author Reshwan Barhoe
 */
object User : IntIdTable() {
    val firstName = varchar("firstName", 255)
    val prefixes = varchar("prefixes", 255)
    val lastName = varchar("lastName", 255)
    val role = customEnumeration(
        name = "role",
        sql = "ENUM(${Roles::class.java.enumConstants.joinToString { "'$it'" }})",
        fromDb = { value -> enumValueOf<Roles>(value as String) },
        toDb = { it.name }
    )
    val email = varchar("email", 255)
    val password = varchar("password", 255)
}