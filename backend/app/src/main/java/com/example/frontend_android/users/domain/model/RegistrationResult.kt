package com.example.frontend_android.users.domain.model

sealed class RegistrationResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : RegistrationResult<T>(data)
    class EmailAlreadyExists<T>(message: String, data: T? = null) :
        RegistrationResult<T>(data, message)

    class UnknownError<T>(message: String, data: T? = null) :
        RegistrationResult<T>(data, message)
}