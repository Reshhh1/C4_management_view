package com.example.frontend_android.c4_model.presentation.mapper

import com.example.frontend_android.c4_model.data.remote.dtos.ElementDTO
import com.example.frontend_android.c4_model.presentation.states.C4ElementState

/**
 * Maps into the element state
 * @author Reshwan Barhoe
 * @return C4ElementState
 */
fun ElementDTO.toC4ElementState(): C4ElementState {
    return C4ElementState(
        id = this.id,
        elementInformation = this,
        contextId = this.contextId,
        containerId = this.containerId
    )
}