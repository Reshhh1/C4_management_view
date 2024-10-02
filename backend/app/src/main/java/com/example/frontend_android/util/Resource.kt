package com.example.frontend_android.util

/**
 * Helps with encapsulation the result of API requests
 * @author Reshwan Barhoe
 * @param data that's being provided in the response
 * @param message that's being provided
 */
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}