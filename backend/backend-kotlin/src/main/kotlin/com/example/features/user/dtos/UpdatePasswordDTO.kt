package com.example.features.user.dtos

import kotlinx.serialization.*

@Serializable
data class UpdatePasswordDTO(val oldPassword: String, val newPassword: String)
