package com.example.frontend_android.c4_model.presentation.states

import com.example.frontend_android.c4_model.data.remote.dtos.ElementDTO

/**
 * The C4 model state of the screen
 * contains the required layers to be previewed
 * @author Reshwan Barhoe
 */
data class C4ModelState(
    val contexts: List<C4ElementState> = listOf(),
    val containers: List<C4ElementState> = listOf(),
)

/**
 * Defines the state of a c4 element that being used in the
 * C4 model state
 * @author Reshwan Barhoe
 */
data class C4ElementState(
    val elementInformation: ElementDTO,
    val id: String,
    val contextId: String?,
    val containerId: String?
)