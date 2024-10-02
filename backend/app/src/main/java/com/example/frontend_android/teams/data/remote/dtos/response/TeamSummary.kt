package com.example.frontend_android.teams.data.remote.dtos.response

import kotlinx.serialization.Serializable

/**
 * Data response model for the team summary.
 * This is being used for showcasing teams.
 * @author Reshwan Barhoe
 */
@Serializable
data class TeamSummary(
    val id: Int,
    val name: String
)
