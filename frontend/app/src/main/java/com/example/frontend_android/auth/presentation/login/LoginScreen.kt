package com.example.frontend_android.auth.presentation.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend_android.R
import com.example.frontend_android.auth.presentation.login.state.LoginState
import com.example.frontend_android.ui.components.ActionButton
import com.example.frontend_android.ui.components.LabelText
import com.example.frontend_android.ui.theme.Secondary

/**
 * The login screen that's going to be used for the navigation
 * @author Reshwan Barhoe
 */
@Composable
fun LoginScreen(
    navigateToDashboard: () -> Unit,
    navigateToFullName: () -> Unit,
    viewModel: LoginViewModel
) {
    if (viewModel.isLoggedIn) {
        navigateToDashboard()
    }
    Column(
        modifier = Modifier
            .padding(
                horizontal = dimensionResource(id = R.dimen.content_width),
                vertical = dimensionResource(id = R.dimen.content_height)
            )
            .fillMaxWidth()
    ) {
        LoginContent(viewModel, navigateToFullName)
    }
}

/**
 * Displays the login content
 * @author Reshwan Barhoe
 * @param viewModel ViewModel of the login screen
 */
@Composable
private fun LoginContent(
    viewModel: LoginViewModel,
    navigateToFullName: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("login"), elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(id = R.dimen.elevation_card)
        ), colors = CardDefaults.cardColors(
            containerColor = Secondary
        ), shape = RectangleShape
    ) {
        Column(
            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 40.dp),
                imageVector = Icons.Sharp.AccountCircle,
                contentDescription = "Account-logo"
            )
            Form(
                loginState = viewModel.loginState,
                onChange = viewModel::onValueChange,
                viewModel,
                navigateToFullName
            )
        }
    }
}

/**
 * Displays the login form
 * @author Reshwan Barhoe
 * @param loginState The login state of the form
 * @param onChange function that's being executed on value change
 * @param viewModel Viewmodel of the login screen
 */
@Composable
private fun Form(
    loginState: LoginState,
    onChange: (LoginState) -> Unit,
    viewModel: LoginViewModel,
    navigateToFullName: () -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.testTag("EmailInput"),
        value = loginState.email,
        onValueChange = { onChange(loginState.copy(email = it)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
        ),
        singleLine = true,
        label = {
            LabelText(
                labelId = stringResource(
                    id = R.string.input_placeholder, "business email"
                )
            )
        })
    if (loginState.emailError?.isNotEmpty() == true) {
        ErrorMessage(loginState.emailError)
    }
    Spacer(modifier = Modifier.padding(5.dp))
    OutlinedTextField(
        modifier = Modifier.testTag("PasswordInput"),
        value = loginState.password,
        onValueChange = { onChange(loginState.copy(password = it)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
        ),
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        label = {
            LabelText(
                labelId = stringResource(
                    id = R.string.input_placeholder, "password"
                )
            )
        })
    if (loginState.passwordError?.isNotEmpty() == true) {
        ErrorMessage(loginState.passwordError)
    }
    if (viewModel.errorMessageState.isNotEmpty()) {
        val context = LocalContext.current
        ErrorMessage(
            message = context.getString(viewModel.errorMessageState.toInt())
        )
    }
    SubmitButton(viewModel)
    RegisterTextButton(navigateToFullName)
}

/**
 * Displays the login form button that
 * performs a onClick action when getting clicked
 * @author Reshwan Barhoe
 * @param viewModel ViewModel of the login screen
 */
@Composable
private fun SubmitButton(
    viewModel: LoginViewModel
) {
    ActionButton(
        buttonName = stringResource(
            id = R.string.login_button
        ),
        onClick = { viewModel.onSubmit() },
        modifier = Modifier
            .padding(top = 40.dp)
            .width(250.dp)
    )
}

/**
 * Button to navigate to the full name screen
 * @author Ömer Aynaci
 * @param navigateToFullName action to navigate to the full name screen
 */
@Composable
private fun RegisterTextButton(navigateToFullName: () -> Unit) {
    ActionButton(
        buttonName = stringResource(R.string.to_full_name_screen),
        onClick = { navigateToFullName() },
        containerColor = ButtonDefaults.buttonColors(containerColor = Color.White),
        textColor = Color.White,
        modifier = Modifier.padding(top = 20.dp)
    )
}

/**
 * Displays error messages related to the form
 * @author Reshwan Barhoe
 * @param message that's being displayed
 */
@Composable
private fun ErrorMessage(message: String) {
    Text(
        text = message, color = MaterialTheme.colorScheme.error, fontSize = 12.sp
    )
}
