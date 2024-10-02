package com.example.frontend_android.teams.domain.errors


/**
 * Hierarchy of the expected API results of the team
 * @author Reshwan Barhoe
 * @param data that's being provided in the response
 * @param message that's being provided
 */
sealed class TeamResource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : TeamResource<T>(data)
    class NameAlreadyExists<T>(message: String, data: T? = null) : TeamResource<T>(data, message)
    class NetworkError<T>(message: String, data: T? = null) : TeamResource<T>(data, message)
}


