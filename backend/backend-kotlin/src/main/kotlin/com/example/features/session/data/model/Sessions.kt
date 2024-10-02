package com.example.features.session.data.model

import com.example.features.user.data.model.User
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

/**
 * the sessions table for storing the tokens
 * @author Ömer Aynaci
 */
object Sessions : Table() {
    val userId = integer("user_id").autoIncrement().references(User.id)
    val token = varchar("token", length = 255)
    val createdAt = datetime("created_at")
    val expireDate = datetime("expire_date")
}