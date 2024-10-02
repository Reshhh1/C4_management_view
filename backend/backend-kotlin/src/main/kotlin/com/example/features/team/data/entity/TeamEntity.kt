package com.example.features.team.data.entity

import com.example.features.team.data.model.Team
import com.example.features.team.data.model.TeamUser
import com.example.features.user.data.entity.UserEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * Represent a team entity,
 * It provides a many-to-many relationship with the TeamUser table
 * @author Reshwan Barhoe
 * @param id the id of the team entity
 */
class TeamEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<TeamEntity>(Team)
    var name by Team.name
    var createdAt by Team.createdAt
    var createdBy by Team.createdBy
    var members by UserEntity via TeamUser
}