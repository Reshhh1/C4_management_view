package com.example.general.exception

import kotlinx.serialization.Serializable

class EntityExistsException(message: String): Exception(message)

@Serializable
data class UnauthorizedError(private val error: String) : IllegalArgumentException(error)