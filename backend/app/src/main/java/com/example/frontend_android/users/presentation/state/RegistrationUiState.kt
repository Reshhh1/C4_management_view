package com.example.frontend_android.users.presentation.state

import com.example.frontend_android.users.data.remote.dtos.request.RegistrationRequest

sealed class RegistrationUiState {
    data class Success(val newUser: RegistrationRequest) : RegistrationUiState()
    data object Loading : RegistrationUiState()
    data object Error : RegistrationUiState()
}