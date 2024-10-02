package com.example.frontend_android.auth.domain.use_case

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String = ""
)
