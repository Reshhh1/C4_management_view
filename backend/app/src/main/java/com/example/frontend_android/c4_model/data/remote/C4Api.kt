package com.example.frontend_android.c4_model.data.remote

import com.example.frontend_android.c4_model.data.remote.dtos.ElementDTO
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * C4 client API
 * @author Reshwan Barhoe
 */
interface C4Api {
    @GET("contexts")
    suspend fun getAllContexts(): List<ElementDTO>

    @GET("contexts/{id}/containers")
    suspend fun getAllContainers(
        @Path("id") id: String
    ): List<ElementDTO>
}