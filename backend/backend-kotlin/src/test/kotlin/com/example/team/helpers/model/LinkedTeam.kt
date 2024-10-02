package com.example.team.helpers.model

/**
 * Object for linking the testTeam and members
 * @author Reshwan Barhoe
 */
data class LinkedTeam(
    val testTeam: TestTeam,
    val members: List<Int>
)
