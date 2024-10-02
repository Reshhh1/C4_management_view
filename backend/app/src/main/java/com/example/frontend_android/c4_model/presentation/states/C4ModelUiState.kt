package com.example.frontend_android.c4_model.presentation.states

/**
 * The C4 context state
 * @author Reshwan Barhoe
 */
sealed class C4ModelUiState {
    data object Loading : C4ModelUiState()
    data object Success : C4ModelUiState()
    data class Error(val errorMessage: String) : C4ModelUiState()
}
