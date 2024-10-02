package com.example.frontend_android.users.presentation.screens.passwordScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend_android.R
import com.example.frontend_android.auth.domain.use_case.ValidationResult
import com.example.frontend_android.ui.components.ActionButton
import com.example.frontend_android.ui.components.BackArrowButton
import com.example.frontend_android.ui.theme.Secondary
import com.example.frontend_android.users.domain.model.RegistrationResult
import com.example.frontend_android.users.presentation.RegistrationViewModel
import com.example.frontend_android.users.presentation.state.RegistrationState

/**
 * Composable for the password screen
 * @author Ömer Aynaci
 * @param registrationViewModel the registration view model
 * @param navigateToLogin navigate to the login screen
 * @param navigateBackToEmail navigate back to the email screen
 * @param goBack navigate back to the previous screen
 */
@Composable
fun PasswordScreen(
    registrationViewModel: RegistrationViewModel,
    navigateToLogin: () -> Unit,
    navigateBackToEmail: () -> Unit,
    goBack: () -> Unit
) {
    val confirmPassword by registrationViewModel.confirmPassword.collectAsState()
    PasswordContent(
        input = registrationViewModel.user,
        confirmPassword = confirmPassword,
        onValueChange = registrationViewModel::onValueChange,
        passwordError = registrationViewModel.getRegistrationError().passwordError,
        onConfirmChange = registrationViewModel::onConfirmPasswordChange,
        onSubmit = { registrationViewModel.createUser() },
        navigateToLogin = { navigateToLogin() },
        registrationViewModel = registrationViewModel,
        navigateBackToEmail = navigateBackToEmail
    )
    BackArrowButton(goBack = { goBack() })
}

/**
 * the password content in the password form
 * @author Ömer Aynaci
 * @param input the password input
 * @param confirmPassword the confirm password input
 * @param onValueChange input password changes on change
 * @param onConfirmChange input confirm password changes on change
 * @param passwordError password error
 * @param onSubmit submit the form
 * @param navigateToLogin navigate to the login screen
 * @param registrationViewModel the registration view model
 * @param navigateBackToEmail navigate back to email screen
 */
@Composable
fun PasswordContent(
    input: RegistrationState,
    confirmPassword: String,
    onValueChange: (RegistrationState) -> Unit,
    passwordError: ValidationResult,
    onConfirmChange: (String) -> Unit,
    onSubmit: () -> Unit,
    navigateToLogin: () -> Unit,
    registrationViewModel: RegistrationViewModel,
    navigateBackToEmail: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 140.dp, top = 40.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.registration_title),
            modifier = Modifier.padding(bottom = 20.dp),
            fontSize = 25.sp
        )
        ElevatedCard(
            modifier = Modifier.size(340.dp, 690.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = dimensionResource(id = R.dimen.elevation_card)
            ), colors = CardDefaults.cardColors(
                containerColor = Secondary
            ), shape = RectangleShape
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.password_title),
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 20.dp)
                )
                Spacer(modifier = Modifier.padding(top = 20.dp))
                PasswordForm(
                    input = input,
                    confirmPassword = confirmPassword,
                    onValueChange = onValueChange,
                    onConfirmChange = onConfirmChange,
                    passwordError = passwordError,
                    onSubmit = onSubmit,
                    navigateToLogin = navigateToLogin,
                    registrationViewModel = registrationViewModel,
                    navigateBackToEmail = navigateBackToEmail
                )
            }
        }
    }
}

/**
 * form for the password screen
 * @author Ömer Aynaci
 * @param input the password input
 * @param confirmPassword the confirm password input
 * @param onValueChange input password changes on change
 * @param onConfirmChange input confirm password changes on change
 * @param passwordError password error
 * @param onSubmit submit the form
 * @param navigateToLogin navigate to the login screen
 * @param navigateBackToEmail navigate back to email screen
 * @param registrationViewModel the registration view model
 */
@Composable
fun PasswordForm(
    input: RegistrationState,
    confirmPassword: String,
    onValueChange: (RegistrationState) -> Unit,
    onConfirmChange: (String) -> Unit,
    passwordError: ValidationResult,
    onSubmit: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateBackToEmail: () -> Unit,
    registrationViewModel: RegistrationViewModel
) {
    val registrationError by registrationViewModel.registrationError.observeAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PasswordTextField(
            input = input,
            onValueChange = onValueChange,
            label = { PasswordLabel() },
            modifier = Modifier.testTag("Password")
        )
        PasswordErrorMessage(password = input.password, error = passwordError)
        Spacer(modifier = Modifier.padding(top = 20.dp))
        ConfirmPasswordTextField(
            confirmPassword = confirmPassword,
            onConfirmChange = onConfirmChange,
            label = { ConfirmPasswordLabel() },
            modifier = Modifier.testTag("ConfirmPassword")
        )
        ConfirmPasswordErrorMessage(password = input.password, confirmPassword = confirmPassword)
        Spacer(modifier = Modifier.padding(top = 20.dp))
        ActionButton(
            buttonName = stringResource(R.string.register_button_text), onClick = {
                if (confirmPassword == input.password) {
                    onSubmit()
                }
            }, modifier = Modifier
                .width(290.dp)
                .padding(top = 20.dp),
            enabled = passwordError.successful && confirmPassword == input.password
        )
        DisplayUnknownErrorMessage(registrationError = registrationError)
    }
    HandleRegistrationResult(
        registrationViewModel = registrationViewModel,
        navigateToLogin = navigateToLogin,
        navigateBackToEmail = navigateBackToEmail,
        onSubmit = onSubmit
    )
}

/**
 * displaying the unknown error message
 * @author Ömer Aynaci
 * @param registrationError the registration validation
 */
@Composable
private fun DisplayUnknownErrorMessage(registrationError: RegistrationResult<String>?) {
    if (registrationError is RegistrationResult.UnknownError) {
        registrationError.message?.let { errorMessage ->
            Text(
                text = errorMessage, color = Color.Red,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

/**
 * Handles the registration result
 * @author Ömer Aynaci
 * @param registrationViewModel the registration view model
 * @param navigateToLogin action to navigate to the login screen
 * @param navigateBackToEmail action to navigate back to the email screen
 * @param onSubmit action for submitting the form
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun HandleRegistrationResult(
    registrationViewModel: RegistrationViewModel,
    navigateToLogin: () -> Unit,
    navigateBackToEmail: () -> Unit,
    onSubmit: () -> Unit
) {
    val registrationError by registrationViewModel.registrationError.observeAsState()

    LaunchedEffect(registrationError) {
        when (registrationError) {
            is RegistrationResult.EmailAlreadyExists -> {
                navigateBackToEmail()
            }

            is RegistrationResult.Success -> {
                registrationViewModel.clearRegistrationError()
                onSubmit()
                navigateToLogin()
                registrationViewModel.resetForm()
            }

            else -> {}
        }
    }
}

/**
 * the password text field
 * @author Ömer Aynaci
 * @param input the password
 * @param onValueChange value changes when user types
 * @param label the input label
 * @param modifier the modifier for styling and etc.
 */
@Composable
fun PasswordTextField(
    input: RegistrationState,
    onValueChange: (RegistrationState) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember {
        mutableStateOf(false)
    }
    val toggleIcon = if (isVisible) VisualTransformation.None else PasswordVisualTransformation()
    OutlinedTextField(
        value = input.password, onValueChange = {
            onValueChange(input.copy(password = it))
        }, visualTransformation = toggleIcon,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        label = label,
        modifier = modifier,
        trailingIcon = {
            PasswordToggle(isPasswordVisible = isVisible, onToggleClick = {
                isVisible = !isVisible
            })
        }
    )
}

/**
 * the confirm password text field
 * @author Ömer Aynaci
 * @param confirmPassword the confirm password
 * @param onConfirmChange value changes when user types
 * @param label the text field label
 * @param modifier the modifier for styling elements
 */
@Composable
fun ConfirmPasswordTextField(
    confirmPassword: String,
    onConfirmChange: (String) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }
    val icon = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    OutlinedTextField(
        value = confirmPassword,
        onValueChange = {
            onConfirmChange(it)
        },
        label = label,
        visualTransformation = icon,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = modifier, trailingIcon = {
            PasswordToggle(isPasswordVisible = isPasswordVisible, onToggleClick = {
                isPasswordVisible = !isPasswordVisible
            })
        }
    )
}

/**
 * label for password text field
 * @author Ömer Aynaci
 */
@Composable
private fun PasswordLabel() {
    Text(text = stringResource(R.string.password_label))
}

/**
 * label for confirm password text field
 * @author Ömer Aynaci
 */
@Composable
private fun ConfirmPasswordLabel() {
    Text(text = stringResource(R.string.confirmPassword_label))
}

/**
 * displaying error message for password if an error occurs
 * @author Ömer Aynaci
 * @param password the password
 * @param error the error that is being occurred
 */
@Composable
private fun PasswordErrorMessage(
    password: String,
    error: ValidationResult,
) {
    if (password.isNotEmpty()) {
        Text(
            text = error.errorMessage,
            color = Color.Red,
            textAlign = TextAlign.Center,
            fontSize = 15.sp
        )
    }
}

/**
 * displaying error message for confirm password if the given confirm password doesn't match with the password
 * @author Ömer Aynaci
 * @param password the password
 * @param confirmPassword the confirm password
 */
@Composable
private fun ConfirmPasswordErrorMessage(
    password: String,
    confirmPassword: String,
) {
    if (confirmPassword.isNotEmpty() && confirmPassword != password) {
        Text(
            text = stringResource(R.string.confirm_password_doesnt_match),
            color = Color.Red,
            textAlign = TextAlign.Center,
            fontSize = 15.sp
        )
    }
}

/**
 * the password show/hide toggle button
 * @author Ömer Aynaci
 * @param isPasswordVisible checks if password is visible
 * @param onToggleClick action to show/hide password
 */
@Composable
private fun PasswordToggle(isPasswordVisible: Boolean, onToggleClick: () -> Unit) {
    val icon = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

    Box(modifier = Modifier.clickable {
        onToggleClick()
    }) {
        Icon(imageVector = icon, contentDescription = "toggle icon")
    }
}