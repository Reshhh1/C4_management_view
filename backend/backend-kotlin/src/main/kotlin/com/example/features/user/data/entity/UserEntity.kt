package com.example.features.user.data.entity

import com.example.features.team.data.entity.*
import com.example.features.team.data.model.*
import com.example.features.user.data.model.*
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.*

/**
 * Represent a user entity,
 * It provides a many-to-many relationship with the TeamUser table
 * @author Reshwan Barhoe
 * @param id the id of the user entity
 */
class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(User)

    var firstName by User.firstName
    var prefixes by User.prefixes
    var lastName by User.lastName
    var role by User.role
    var email by User.email
    var password by User.password

    @Suppress("unused")
    var teams by TeamEntity via TeamUser
}