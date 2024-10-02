package com.example.frontend_android.auth.data.remote

import com.example.frontend_android.auth.data.remote.dtos.request.AuthenticationRequest
import com.example.frontend_android.auth.data.remote.dtos.response.AuthenticationMessage
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface SessionApi {
    @POST("sessions")
    suspend fun login(@Body loginRequest: AuthenticationRequest): Response<Unit>

    @GET("users/firstname")
    suspend fun getUserFirstName(@Header("Authorization") token: String): Response<AuthenticationMessage>
}