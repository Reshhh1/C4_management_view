package com.example.frontend_android.users.presentation.screens.emailScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
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
 * composable for the email screen
 * @author Ömer Aynaci
 * @param registrationViewModel the registration view model
 * @param onNext action for navigating to next screen
 * @param goBack action for navigating back to previous screen
 */
@Composable
fun EmailScreen(
    registrationViewModel: RegistrationViewModel,
    onNext: () -> Unit,
    goBack: () -> Unit
) {
    EmailContent(
        input = registrationViewModel.user,
        navigateToNextScreen = { onNext() },
        registrationViewModel = registrationViewModel,
        onValueChange = registrationViewModel::onValueChange
    )
    BackArrowButton(goBack = { goBack() })
}

/**
 * composable for the card with the email and button to navigate to next screen
 * @author Ömer Aynaci
 * @param input the email
 * @param navigateToNextScreen action to navigate to next screen
 * @param registrationViewModel the registration view model
 * @param onValueChange value changes when typing
 */
@Composable
fun EmailContent(
    input: RegistrationState,
    navigateToNextScreen: () -> Unit,
    registrationViewModel: RegistrationViewModel,
    onValueChange: (RegistrationState) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 140.dp, top = 40.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Registration", modifier = Modifier.padding(bottom = 20.dp), fontSize = 25.sp)
        ElevatedCard(
            modifier = Modifier.size(340.dp, 690.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = dimensionResource(id = R.dimen.elevation_card)
            ), colors = CardDefaults.cardColors(
                containerColor = Secondary
            ), shape = RectangleShape
        ) {
            EmailForm(
                input = input,
                onValueChange = onValueChange,
                registrationViewModel = registrationViewModel,
                onNext = {
                    if (registrationViewModel.getRegistrationError().emailError.successful) {
                        navigateToNextScreen()
                    }
                })
        }
    }
}

/**
 * the form for the email
 * @author Ömer Aynaci
 * @param input the email
 * @param onValueChange when user types the input changes
 * @param registrationViewModel the view model
 * @param onNext action for the button this leads to the next screen
 */
@Composable
fun EmailForm(
    input: RegistrationState,
    onValueChange: (RegistrationState) -> Unit,
    registrationViewModel: RegistrationViewModel,
    onNext: () -> Unit
) {
    val registrationError by registrationViewModel.registrationError.observeAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.email_title),
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 20.dp)
        )
        OutlinedTextField(
            value = input.email,
            onValueChange = { email ->
                onValueChange(input.copy(email = email))
            },
            modifier = Modifier
                .padding(top = 30.dp)
                .testTag("EmailAddress"),
            label = { EmailAddressLabel() },
            supportingText = { EmailAlreadyExistsErrorMessage(registrationError = registrationError) })
        EmailErrorMessage(
            email = input.email,
            error = registrationViewModel.getRegistrationError().emailError
        )

        ActionButton(
            buttonName = stringResource(R.string.next_button_text), onClick = {
                if (registrationViewModel.getRegistrationError().emailError.successful) {
                    registrationViewModel.clearRegistrationError()
                    onNext()
                }
            }, modifier = Modifier
                .width(290.dp)
                .padding(top = 50.dp),
            enabled = registrationViewModel.getRegistrationError().emailError.successful
        )
    }
}

/**
 * displaying the error for email already exists
 * @author Ömer Aynaci
 * @param registrationError the registration validation
 */
@Composable
private fun EmailAlreadyExistsErrorMessage(registrationError: RegistrationResult<String>?) {
    if (registrationError is RegistrationResult.EmailAlreadyExists) {
        registrationError.message?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}


/**
 * composable for email label
 * @author Ömer Aynaci
 */
@Composable
private fun EmailAddressLabel() {
    Text(text = stringResource(R.string.email_label))
}

/**
 *
 * UI element for displaying error messages for email
 * @author Ömer Aynaci
 * @param email the email input
 * @param error the error that is being occurred
 */
@Composable
private fun EmailErrorMessage(
    email: String,
    error: ValidationResult,
) {
    if (email.isNotEmpty()) {
        Text(
            text = error.errorMessage,
            color = Color.Red,
            modifier = Modifier.padding(start = 35.dp),
            fontSize = 15.sp
        )
    }
}