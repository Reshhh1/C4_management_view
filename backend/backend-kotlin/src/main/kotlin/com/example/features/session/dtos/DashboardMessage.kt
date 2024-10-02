package com.example.features.session.dtos

import kotlinx.serialization.*

@Serializable
data class DashboardMessage(override val message: String) : IllegalArgumentException(message)