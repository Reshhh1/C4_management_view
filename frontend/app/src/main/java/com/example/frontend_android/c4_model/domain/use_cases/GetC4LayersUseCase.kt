package com.example.frontend_android.c4_model.domain.use_cases

import com.example.frontend_android.c4_model.data.remote.dtos.ElementDTO
import com.example.frontend_android.c4_model.data.repository.C4Repository
import com.example.frontend_android.util.Resource
import javax.inject.Inject

class GetC4LayersUseCase @Inject constructor(
    private val c4Repository: C4Repository
) {

    /**
     * Executes a method from the data layer to retrieve the
     * context layer
     * @author Reshwan Barhoe
     * @return result of the requested data
     */
    suspend fun getContextLayer(): Resource<List<ElementDTO>> {
        return c4Repository.getAllContextPreviews()
    }

    /**
     * Executes a method from the data layer to retrieve the
     * container layer of a specific context
     * @author Reshwan Barhoe
     * @param contextId that's being used to specify the context
     * @return result of the requested data
     */
    suspend fun getContainerLayer(contextId: String): Resource<List<ElementDTO>> {
        return c4Repository.getAllContainerPreviews(contextId)
    }
}