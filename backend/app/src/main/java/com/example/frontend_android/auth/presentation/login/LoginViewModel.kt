package com.example.frontend_android.auth.presentation.login


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend_android.R
import com.example.frontend_android.auth.data.remote.dtos.request.AuthenticationRequest
import com.example.frontend_android.auth.data.remote.dtos.response.AuthenticationMessage
import com.example.frontend_android.auth.data.repository.SessionRepository
import com.example.frontend_android.auth.domain.AuthenticationResult
import com.example.frontend_android.auth.domain.use_case.ValidateLogin
import com.example.frontend_android.auth.domain.use_case.model.LoginErrors
import com.example.frontend_android.auth.presentation.AuthenticatedUserState
import com.example.frontend_android.auth.presentation.login.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {
    private val _userState =
        MutableStateFlow<AuthenticatedUserState>(AuthenticatedUserState.Loading)
    val userState: StateFlow<AuthenticatedUserState> get() = _userState
    private val validateLogin = ValidateLogin()
    var loginState by mutableStateOf(LoginState())
    var errorMessageState by mutableStateOf("")
    var isLoggedIn by mutableStateOf(false)


    /**
     * updating the user state
     * @author Ömer Aynaci
     * @param newState the updated state
     */
    private fun updateUserState(newState: AuthenticatedUserState) {
        _userState.value = newState
    }

    /**
     * Starts the login process using the provided login form data.
     * Upon completion, the authResult is being handled
     * @author Reshwan Barhoe
     * @param loginForm login data that's being passed
     */
    private fun login(loginForm: AuthenticationRequest) {
        viewModelScope.launch {
            val authenticationResult = sessionRepository.login(loginForm)
            val login = sessionRepository.loggedIn(loginForm)
            handleAuthResult(authenticationResult)
            if (login.isSuccessful) {
                loginUser(loginForm)
            }
        }
    }


    /**
     * logs the user in afterwards it fetches the token from the cookies
     * @author Ömer Aynaci
     * @param loginForm the login form
     */
    private fun loginUser(loginForm: AuthenticationRequest) {
        viewModelScope.launch {
            try {
                val response = sessionRepository.loggedIn(loginForm)
                isResponseSuccessful(response)
            } catch (error: HttpException) {
                updateUserState(AuthenticatedUserState.Error)
            }
        }
    }

    /**
     * checks if the response is successful
     * @author Ömer Aynaci
     * @param response the response
     */
    private suspend fun isResponseSuccessful(response: Response<Unit>) {
        if (response.isSuccessful) {
            val token = response.headers()["set-Cookie"]
            val sessionIdToken = sessionRepository.fetchSessionIdToken(token)
            sessionRepository.saveAuthToken(token)
            fetchAuthenticatedUser("Bearer $sessionIdToken")
        } else {
            updateUserState(AuthenticatedUserState.Error)
        }

    }

    /**
     * fetch the authenticated user
     * @author Ömer Aynaci
     * @param token the jwt token
     */
    private fun fetchAuthenticatedUser(token: String?) {
        viewModelScope.launch {
            try {
                if (token != null) {
                    getUserFirstNameIfAuthenticated(token)
                }
            } catch (error: Exception) {
                updateUserState(AuthenticatedUserState.Error)
            }
        }
    }

    /**
     * checking if the user is authenticated if so then update the state to success
     * @author Ömer Aynaci
     * @param token the jwt token
     * @return authentication message
     */
    private suspend fun getUserFirstNameIfAuthenticated(token: String): AuthenticationMessage? {
        val authenticatedUserResponse = sessionRepository.getUserFirstName(token)
        val authenticatedUserDetails = authenticatedUserResponse.body()
        try {
            isAuthenticationSuccessful(authenticatedUserResponse)
        } catch (error: HttpException) {
            updateUserState(AuthenticatedUserState.Error)
        }
        return authenticatedUserDetails
    }

    /**
     * checks if the authentication is successful
     * @author Ömer Aynaci
     * @param authenticatedUserResponse the authentication response
     */
    private fun isAuthenticationSuccessful(
        authenticatedUserResponse: Response<AuthenticationMessage>,
    ) {
        val responseBody = authenticatedUserResponse.body()
        if (authenticatedUserResponse.isSuccessful) {
            updateUserState(AuthenticatedUserState.Success(responseBody))
        } else {
            updateUserState(AuthenticatedUserState.Error)
        }
    }

    /**
     * Handles the AuthResult for certain scenario's
     * @author Reshwan Barhoe
     * @param authenticationResult AuthResult that's being handled
     */
    private fun handleAuthResult(authenticationResult: AuthenticationResult<Unit>) {
        when (authenticationResult) {
            is AuthenticationResult.Authorized -> isLoggedIn = true
            is AuthenticationResult.UnAuthorized -> setErrorMessage(R.string.login_invalid_credentials.toString())
            else -> setErrorMessage(R.string.error_message.toString())
        }
    }

    /**
     * Sets the new response message state
     * @author Reshwan Barhoe
     * @param message: message the state is being set to
     */
    private fun setErrorMessage(message: String) {
        errorMessageState = message
    }

    /**
     * Calls the login method if the login state is valid
     * @author Reshwan Barhoe
     */
    fun onSubmit() {
        if (validateAndUpdateLoginState()) {
            login(
                AuthenticationRequest(
                    email = loginState.email,
                    password = loginState.password
                )
            )
        }
    }

    /**
     * Validates the login state and updates the login state.
     * Validates based on the successful key
     * @author Reshwan Barhoe
     * @return if the errors are successful or not
     */
    private fun validateAndUpdateLoginState(): Boolean {
        val loginErrors = getLoginErrors()
        updateLoginStateWithErrors(
            loginErrors.emailError.errorMessage,
            loginErrors.passwordError.errorMessage
        )
        return loginErrors.emailError.successful && loginErrors.passwordError.successful
    }

    /**
     * Returns the login errors
     * @author Reshwan Barhoe
     * @return The errors inside a LoginErrors object
     */
    private fun getLoginErrors(): LoginErrors {
        val emailResult = validateLogin.validateEmail(loginState.email)
        val passwordResult = validateLogin.validatePassword(loginState.password)
        return LoginErrors(
            emailError = emailResult,
            passwordError = passwordResult
        )
    }

    /**
     * Updates the login state which is focused on the errors
     * @author Reshwan Barhoe
     * @param emailError Email error that's being set
     * @param passwordError Password error that's being set
     */
    private fun updateLoginStateWithErrors(
        emailError: String,
        passwordError: String
    ) {
        loginState = loginState.copy(
            emailError = emailError,
            passwordError = passwordError
        )
    }

    /**
     * Updates the login state, this method is used in the screen
     * itself to update the state. It's also being validated
     * for a dynamic validation look for the user
     * @author Reshwan Barhoe
     * @param loginState The login state
     */
    fun onValueChange(loginState: LoginState) {
        this.loginState = loginState
        resetErrorMessage()
        validateAndUpdateLoginState()
    }

    /**
     * Resets the response message
     * @author Reshwan Barhoe
     */
    private fun resetErrorMessage() {
        errorMessageState = ""
    }
}