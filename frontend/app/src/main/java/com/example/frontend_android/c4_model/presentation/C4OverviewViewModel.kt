package com.example.frontend_android.c4_model.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend_android.c4_model.domain.use_cases.GetC4LayersUseCase
import com.example.frontend_android.c4_model.presentation.mapper.toC4ElementState
import com.example.frontend_android.c4_model.presentation.states.C4ElementState
import com.example.frontend_android.c4_model.presentation.states.C4ModelState
import com.example.frontend_android.c4_model.presentation.states.C4ModelUiState
import com.example.frontend_android.util.Constants
import com.example.frontend_android.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class C4OverviewViewModel @Inject constructor(
    private val getC4LayersUseCase: GetC4LayersUseCase
) : ViewModel() {
    private val _c4ModelUiState = mutableStateOf<C4ModelUiState>(C4ModelUiState.Loading)
    val c4ModelUiState: State<C4ModelUiState> = _c4ModelUiState

    private val _c4ModelState = mutableStateOf(C4ModelState())
    val c4ModelState = _c4ModelState

    private val _selectedElementId = mutableStateOf("")

    /**
     * Fetches the team data when the view model is being instantiated
     * @author Reshwan Barhoe
     */
    init {
        getContexts()
    }

    /**
     * Attempts to fetch the contexts.
     * Handles based on the returned result
     * @author Reshwan Barhoe
     */
    private fun getContexts() {
        viewModelScope.launch {
            when (val result = getC4LayersUseCase.getContextLayer()) {
                is Resource.Success -> result.data?.let { contexts ->
                    updateC4ContextState(
                        contexts.map { it.toC4ElementState() })
                    updateC4UiState(C4ModelUiState.Success)
                }

                is Resource.Error -> updateC4UiState(
                    C4ModelUiState.Error(Constants.NETWORK_ERROR)
                )
            }
        }
    }

    /**
     * Attempts to fetch the containers of the provided parent wid.
     * Handles based on the returned result
     * @author Reshwan Barhoe
     * @param contextId parent id
     */
    fun getContainersByContextId(contextId: String) {
        viewModelScope.launch {
            when (val result = getC4LayersUseCase.getContainerLayer(contextId)) {
                is Resource.Success -> result.data?.let { contexts ->
                    updateC4ContainerState(
                        contexts.map { it.toC4ElementState() })
                    updateC4UiState(C4ModelUiState.Success)
                }

                is Resource.Error -> {
                    C4ModelUiState.Error(Constants.NETWORK_ERROR)
                }
            }
        }
    }

    /**
     * Updates the ui state to the provided state
     * @author Reshwan Barhoe
     * @param state that contains the new information
     */
    private fun updateC4UiState(state: C4ModelUiState) {
        _c4ModelUiState.value = state
    }

    /**
     * Updates the C4ContextState
     * @author Reshwan Barhoe
     * @param state renewed C4ContextState
     */
    private fun updateC4ContextState(state: List<C4ElementState>) {
        _c4ModelState.value = _c4ModelState.value.copy(
            contexts = state
        )
    }

    /**
     * Updates the C4 container state
     * @author Reshwan Barhoe
     * @param state renewed C4ContextState
     */
    private fun updateC4ContainerState(state: List<C4ElementState>) {
        _c4ModelState.value = _c4ModelState.value.copy(
            containers = state
        )
    }

    /**
     * Checks if the provided id is expanded
     * @author Reshwan Barhoe
     * @return if the row is expanded
     */
    fun isRowExpanded(id: String): Boolean {
        return _selectedElementId.value == id
    }

    /**
     * Toggles the expansion of a element
     * Brings it to a empty string or the provided id, depending
     * if the provided value already was assigned.
     * @author Reshwan Barhoe
     * @param id element id that being toggled
     */
    fun toggleElementExpansionById(id: String) {
        if (_selectedElementId.value == id) {
            _selectedElementId.value = ""
            updateC4ContainerState(listOf())
        } else {
            _selectedElementId.value = id
        }
    }
}