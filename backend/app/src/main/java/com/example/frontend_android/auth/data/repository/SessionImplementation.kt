package com.example.frontend_android.auth.data.repository

import android.content.Context
import androidx.core.content.edit
import com.example.frontend_android.auth.data.remote.SessionApi
import com.example.frontend_android.auth.data.remote.dtos.request.AuthenticationRequest
import com.example.frontend_android.auth.data.remote.dtos.response.AuthenticationMessage
import com.example.frontend_android.auth.domain.AuthenticationResult
import retrofit2.Response

class SessionImplementation(
    private val sessionApi: SessionApi,
    private val context: Context
) : SessionRepository {

    /**
     * Method that calls the session API responsible for sending
     * the login request
     * @author Reshwan Barhoe
     * @param authenticationRequest required request body that's being send
     * @return a AuthResult depending on the response status code
     */
    override suspend fun login(authenticationRequest: AuthenticationRequest): AuthenticationResult<Unit> {
        return getAuthenticationResult(sessionApi.login(authenticationRequest).code())
    }

    /**
     * logs the user and then adds the token to the cookies
     * @author Ömer Aynaci
     * @param authenticationRequest the authenticaton request body
     * @return the response
     */
    override suspend fun loggedIn(authenticationRequest: AuthenticationRequest): Response<Unit> {
        return sessionApi.login(authenticationRequest)
    }


    /**
     * gets the first name of the user
     * @author Ömer Aynaci
     * @param token the jwt token
     * @return the response message
     */
    override suspend fun getUserFirstName(token: String): Response<AuthenticationMessage> {
        return sessionApi.getUserFirstName(token)
    }

    /**
     * Gets the correct AuthResult depending on the status code
     * @author Reshwan Barhoe
     * @param statusCode response stats code
     * @return a AuthResult depending on the response status code
     */
    private fun getAuthenticationResult(statusCode: Int): AuthenticationResult<Unit> {
        return when (statusCode) {
            200 -> AuthenticationResult.Authorized()
            401 -> AuthenticationResult.UnAuthorized()
            else -> AuthenticationResult.UnknownError()
        }
    }

    /**
     * stores the token in shared preferences
     * @author Ömer Aynaci
     * @param token the jwt token
     */
    override suspend fun saveAuthToken(token: String?) {
        context.getSharedPreferences("token", Context.MODE_PRIVATE)
            .edit {
                putString("auth_token", token)
            }
    }

    /**
     * fetching the token from the cookie header
     * @author Ömer Aynaci
     * @param cookieHeader the cookie header
     * @return the cookie header
     */
    override fun fetchSessionIdToken(cookieHeader: String?): String? {
        if (cookieHeader != null) {
            val sessionIdStartIndex = cookieHeader.indexOf("token=") + 6
            val sessionIdEndIndex = cookieHeader.indexOf(";", startIndex = sessionIdStartIndex)
            if (sessionIdStartIndex != -1 && sessionIdEndIndex != -1) {
                return cookieHeader.substring(
                    sessionIdStartIndex,
                    sessionIdEndIndex
                )
            }
        }
        return cookieHeader
    }
}
