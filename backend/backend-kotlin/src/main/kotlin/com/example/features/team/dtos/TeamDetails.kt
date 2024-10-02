package com.example.features.team.dtos

import com.example.features.user.dtos.UserSummary
import kotlinx.serialization.Serializable

/**
 * DTO structure for displaying the team details
 * @author Reshwan Barhoe
 */
@Serializable
data class TeamDetails(
    val name: String,
    val members: List<UserSummary>,
    val createdAt: String
)