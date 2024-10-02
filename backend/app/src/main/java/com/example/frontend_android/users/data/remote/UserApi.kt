package com.example.frontend_android.users.data.remote

import com.example.frontend_android.users.data.remote.dtos.UserSummary
import com.example.frontend_android.users.data.remote.dtos.request.RegistrationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Interface for the user API
 * @author Reshwan Barhoe
 */
interface UserApi {
    @POST("users")
    suspend fun createUser(@Body newUser: RegistrationRequest): Response<Unit>

    @GET("users")
    suspend fun getUserByName(@Query("name") nameValue: String): Response<List<UserSummary>>
}