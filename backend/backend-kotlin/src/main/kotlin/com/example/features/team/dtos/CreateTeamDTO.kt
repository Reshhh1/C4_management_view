package com.example.features.team.dtos

import kotlinx.serialization.Serializable

/**
 * Expected request body for creating a team
 * @author Reshwan Barhoe
 */
@Serializable
data class CreateTeamDTO(
    val name: String,
    val members: List<Int>
)
