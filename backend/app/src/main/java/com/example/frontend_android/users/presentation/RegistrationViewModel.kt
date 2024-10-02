package com.example.frontend_android.users.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend_android.auth.domain.use_case.ValidationResult
import com.example.frontend_android.users.data.remote.dtos.request.RegistrationRequest
import com.example.frontend_android.users.domain.RegistrationValidator
import com.example.frontend_android.users.domain.model.RegistrationErrors
import com.example.frontend_android.users.domain.model.RegistrationResult
import com.example.frontend_android.users.domain.use_case.CreateUserUseCase
import com.example.frontend_android.users.presentation.state.RegistrationState
import com.example.frontend_android.users.presentation.state.RegistrationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {

    var user by mutableStateOf(RegistrationState())
    private val _registrationUiState =
        MutableStateFlow<RegistrationUiState>(RegistrationUiState.Loading)

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> get() = _confirmPassword
    private val registrationValidator = RegistrationValidator()

    private val _registrationError = MutableLiveData<RegistrationResult<String>>()
    val registrationError: LiveData<RegistrationResult<String>> = _registrationError

    /**
     * changes user value when input value changes
     * @author Ömer Aynaci
     * @param newUser the registration state model
     */
    fun onValueChange(newUser: RegistrationState) {
        user = newUser
    }

    /**
     * changes user value when input value changes for confirm password
     * @author Ömer Aynaci
     * @param confirmPassword the confirm password
     */
    fun onConfirmPasswordChange(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
    }

    /**
     * gets the errors for email
     * @author Ömer Aynaci
     * @return an instance of ValidationResult
     */
    private fun getEmailAddressError(): ValidationResult {
        return registrationValidator.validateEmail(user.email)
    }

    /**
     * gets the errors for password
     * @author Ömer Aynaci
     * @return an instance of ValidationResult
     */
    private fun getPasswordError(): ValidationResult {
        return registrationValidator.validatePassword(user.password)
    }

    /**
     * updating the ui state
     * @author Ömer Aynaci
     * @param newState the new UI state
     */
    private fun updateUiState(newState: RegistrationUiState) {
        _registrationUiState.value = newState
    }

    /**
     * gets the registration errors
     * @author Ömer Aynaci
     * @return an instance of RegistrationErrors
     */
    fun getRegistrationError(): RegistrationErrors {
        val firstNameResult = registrationValidator.validateFirstName(user.firstName)
        val prefixesResult = registrationValidator.validatePrefixes(user.prefixes)
        val lastNameResult = registrationValidator.validateLastName(user.lastName)
        return RegistrationErrors(
            firstNameError = firstNameResult,
            prefixesError = prefixesResult,
            lastNameError = lastNameResult,
            emailError = getEmailAddressError(),
            passwordError = getPasswordError()
        )
    }

    /**
     * validates the full name
     * @author Ömer Aynaci
     * @return true if the full name is valid otherwise false
     */
    fun validateFullName(): Boolean {
        val firstNameResult = registrationValidator.validateFirstName(user.firstName)
        val prefixesResult = registrationValidator.validatePrefixes(user.prefixes)
        val lastNameResult = registrationValidator.validateLastName(user.lastName)
        return firstNameResult.successful && prefixesResult.successful && lastNameResult.successful
    }

    /**
     * registration body
     * @author Ömer Aynaci
     * @return an instance of RegistrationRequest
     */
    private fun createRegistrationRequest(): RegistrationRequest {
        return RegistrationRequest(
            user.firstName,
            user.lastName,
            user.prefixes,
            user.email,
            user.password
        )
    }


    /**
     * creates a new user
     * @author Ömer Aynaci
     */
    fun createUser() {
        viewModelScope.launch {
            when (val result = createUserUseCase.createUser(createRegistrationRequest())) {
                is RegistrationResult.EmailAlreadyExists -> {
                    _registrationError.value = result
                }

                is RegistrationResult.Success -> {
                    _registrationError.value = result
                    handleSuccessfulCreation(createRegistrationRequest())
                }

                is RegistrationResult.UnknownError -> {
                    _registrationError.value = result
                }
            }
        }
    }

    /**
     * handles successful creation
     * @author Ömer Aynaci
     * @param newUser the registration body
     */
    private fun handleSuccessfulCreation(newUser: RegistrationRequest) {
        updateUiState(RegistrationUiState.Success(newUser))
        resetForm()
    }

    /**
     * clears the registration error
     * @author Ömer Aynaci
     */
    @SuppressLint("NullSafeMutableLiveData")
    fun clearRegistrationError() {
        _registrationError.value = null
    }

    /**
     * resets the form
     * @author Ömer Aynaci
     */
    fun resetForm() {
        user = RegistrationState("", "", "", "", "")
        _confirmPassword.value = ""
    }
}