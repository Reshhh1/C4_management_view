package com.example.features.team.data.model

import com.example.features.user.data.model.*
import org.jetbrains.exposed.dao.id.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.*
import java.time.*

/**
 * The team table of the database.
 * Stores basic information about the team
 * @author Reshwan Barhoe
 */
object Team: IntIdTable() {
    val name = varchar("name", 255)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val createdBy = integer("created_by").references(User.id)
}

/**
 * The many-to-many relationship table between the
 * team and the user
 * @author Reshwan Barhoe
 */
object TeamUser: Table() {
   private val teamId = reference("team_id", Team)
   private val userId = reference("user_id", User)
    override val primaryKey = PrimaryKey(teamId, userId, name = "PK_team_user")
}