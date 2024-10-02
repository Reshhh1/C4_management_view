package com.example.frontend_android.auth.data.remote.dtos.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationMessage(
    val message: String
)
