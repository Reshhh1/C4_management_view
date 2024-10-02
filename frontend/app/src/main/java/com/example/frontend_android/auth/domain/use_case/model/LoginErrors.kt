package com.example.frontend_android.auth.domain.use_case.model

import com.example.frontend_android.auth.domain.use_case.ValidationResult

data class LoginErrors(
    val emailError: ValidationResult,
    val passwordError: ValidationResult
)
