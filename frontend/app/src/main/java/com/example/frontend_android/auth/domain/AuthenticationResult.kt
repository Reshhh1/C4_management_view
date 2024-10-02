package com.example.frontend_android.auth.domain

sealed class AuthenticationResult<T>(val data: T? = null) {
    class Authorized<T>(data: T? = null) : AuthenticationResult<T>(data)
    class UnAuthorized<T>(data: T? = null) : AuthenticationResult<T>(data)
    class UnknownError<T>(data: T? = null) : AuthenticationResult<T>(data)
}