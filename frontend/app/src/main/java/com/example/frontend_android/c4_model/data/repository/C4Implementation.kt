package com.example.frontend_android.c4_model.data.repository

import com.example.frontend_android.c4_model.data.remote.C4Api
import com.example.frontend_android.c4_model.data.remote.dtos.ElementDTO
import com.example.frontend_android.util.Constants
import com.example.frontend_android.util.Resource

class C4Implementation(
    private val c4Api: C4Api
) : C4Repository {

    /**
     * Attempts to fetch the required context information
     * from the existing API
     * @author Reshwan Barhoe
     * @return the outcome of the request
     */
    override suspend fun getAllContextPreviews(): Resource<List<ElementDTO>> {
        return try {
            val result = c4Api.getAllContexts()
            Resource.Success(result)
        } catch (exception: Exception) {
            Resource.Error(Constants.NETWORK_ERROR)
        }
    }

    /**
     * Attempts to fetch specific containers information depending
     * on the given parent id from the existing API
     * @author Reshwan Barhoe
     * @param contextId parent id
     * @return the outcome of the request
     */
    override suspend fun getAllContainerPreviews(contextId: String): Resource<List<ElementDTO>> {
        return try {
            val result = c4Api.getAllContainers(contextId)
            Resource.Success(result)
        } catch (exception: Exception) {
            Resource.Error(Constants.NETWORK_ERROR)
        }
    }
}