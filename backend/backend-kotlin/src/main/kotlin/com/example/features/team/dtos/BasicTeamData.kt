package com.example.features.team.dtos

import java.time.LocalDateTime

/**
 * Basic data of a team
 * @author Reshwan Barhoe
 */
data class BasicTeamData(
    val name: String,
    val createdBy: Int,
    val createdAt: LocalDateTime
)