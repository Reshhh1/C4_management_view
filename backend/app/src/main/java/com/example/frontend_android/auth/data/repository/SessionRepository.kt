package com.example.frontend_android.auth.data.repository

import com.example.frontend_android.auth.data.remote.dtos.request.AuthenticationRequest
import com.example.frontend_android.auth.data.remote.dtos.response.AuthenticationMessage
import com.example.frontend_android.auth.domain.AuthenticationResult
import retrofit2.Response

interface SessionRepository {
    suspend fun login(authenticationRequest: AuthenticationRequest): AuthenticationResult<Unit>
    suspend fun loggedIn(authenticationRequest: AuthenticationRequest): Response<Unit>
    suspend fun getUserFirstName(token: String): Response<AuthenticationMessage>

    suspend fun saveAuthToken(token: String?)

    fun fetchSessionIdToken(cookieHeader: String?): String?
}