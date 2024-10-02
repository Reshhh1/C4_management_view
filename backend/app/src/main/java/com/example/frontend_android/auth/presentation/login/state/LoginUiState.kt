package com.example.frontend_android.auth.presentation.login.state

data class LoginState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null
)
