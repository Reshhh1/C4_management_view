package com.example.frontend_android.c4_model.data.repository

import com.example.frontend_android.c4_model.data.remote.dtos.ElementDTO
import com.example.frontend_android.util.Resource

/**
 * Repository interface of the C4 model
 * @author Reshwan Barhoe
 */
interface C4Repository {
    suspend fun getAllContextPreviews(): Resource<List<ElementDTO>>
    suspend fun getAllContainerPreviews(contextId: String): Resource<List<ElementDTO>>
}