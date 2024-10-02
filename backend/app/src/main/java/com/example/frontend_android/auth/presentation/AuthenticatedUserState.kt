package com.example.frontend_android.auth.presentation

import com.example.frontend_android.auth.data.remote.dtos.response.AuthenticationMessage

/**
 * the state of authentication
 * @author Ömer Aynaci
 */
sealed class AuthenticatedUserState {
    data class Success(val user: AuthenticationMessage?) : AuthenticatedUserState()
    data object Loading : AuthenticatedUserState()
    data object Error : AuthenticatedUserState()
}