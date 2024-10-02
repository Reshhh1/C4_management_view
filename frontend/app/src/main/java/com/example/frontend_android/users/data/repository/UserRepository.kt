package com.example.frontend_android.users.data.repository

import com.example.frontend_android.users.data.remote.dtos.UserSummary
import com.example.frontend_android.users.data.remote.dtos.request.RegistrationRequest
import com.example.frontend_android.users.domain.model.RegistrationResult
import com.example.frontend_android.util.Resource

interface UserRepository {
    suspend fun getUsersBySearchTerm(searchName: String): Resource<List<UserSummary>>
    suspend fun createUser(newUser: RegistrationRequest): RegistrationResult<String>
}