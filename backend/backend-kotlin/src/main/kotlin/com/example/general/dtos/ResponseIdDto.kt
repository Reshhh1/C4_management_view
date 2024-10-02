package com.example.general.dtos

import kotlinx.serialization.Serializable

/**
 * The response dto for id responses
 * @author Reshwan Barhoe
 */
@Serializable
data class ResponseIdDto(
    val id: Int
)