package com.example.features.team.dtos

import kotlinx.serialization.Serializable

/**
 * DTO that's being used for displaying
 * a list of teams
 * @author Reshwan Barhoe
 */
@Serializable
data class TeamSummary(
    val id: Int,
    val name: String
)
