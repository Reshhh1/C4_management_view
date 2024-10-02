package com.example.features.session.data.repository

import com.example.features.session.data.model.*
import com.example.util.*
import org.jetbrains.exposed.sql.*
import java.time.*

class SessionRepository {

    /**
     * creates a token
     * @author Ömer Aynaci
     * @param token the generated token
     * @param userId the id of the user
     * @param expireDate the expire date of the session
     */
    suspend fun createSession(token: String, userId: Int, expireDate: LocalDateTime) = DatabaseFactory.dbQuery {
        Sessions.insert {
            it[Sessions.userId] = userId
            it[Sessions.token] = token
            it[createdAt] = LocalDateTime.now()
            it[Sessions.expireDate] = expireDate
        }
    }
}