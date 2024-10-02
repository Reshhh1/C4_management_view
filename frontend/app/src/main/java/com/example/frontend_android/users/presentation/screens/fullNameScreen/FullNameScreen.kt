package com.example.frontend_android.users.presentation.screens.fullNameScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend_android.R
import com.example.frontend_android.auth.domain.use_case.ValidationResult
import com.example.frontend_android.ui.components.ActionButton
import com.example.frontend_android.ui.theme.Secondary
import com.example.frontend_android.users.presentation.RegistrationViewModel
import com.example.frontend_android.users.presentation.state.RegistrationState

/**
 * composable for displaying the full name screen
 * @author Ömer Aynaci
 * @param registrationViewModel the registration view model
 * @param onNext action to navigate to the next screen
 */
@Composable
fun FullNameScreen(registrationViewModel: RegistrationViewModel, onNext: () -> Unit) {
    FullNameContent(
        input = registrationViewModel.user,
        navigateToNextScreen = { onNext() },
        registrationViewModel = registrationViewModel,
        onValueChange = registrationViewModel::onValueChange,
    )
}

/**
 * composable for the full name content, so it shows the full name form in a card
 * @author Ömer Aynaci
 * @param input the full name input
 * @param navigateToNextScreen navigate to the next screen
 * @param registrationViewModel the registration view model
 * @param onValueChange the action when the input value changes
 */
@Composable
private fun FullNameContent(
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
        Text(
            text = stringResource(R.string.registration_title),
            modifier = Modifier.padding(bottom = 20.dp),
            fontSize = 25.sp
        )
        ElevatedCard(
            modifier = Modifier.size(350.dp, 690.dp),
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
                    text = stringResource(R.string.full_name_title),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 20.dp),
                    fontSize = 22.sp
                )
                Spacer(modifier = Modifier.padding(top = 20.dp))
                FullNameForm(
                    input = input,
                    registrationViewModel = registrationViewModel,
                    onValueChange = onValueChange,
                    onNext = {
                        if (registrationViewModel.validateFullName()) {
                            navigateToNextScreen()
                        }
                    })
            }
        }
    }
}

/**
 * composable for displaying the full name form
 * @author Ömer Aynaci
 * @param input the full name inputs
 * @param registrationViewModel the registration view model
 * @param onValueChange the action when the input value changes
 * @param onNext the action to navigate to the next screen
 */
@Composable
private fun FullNameForm(
    input: RegistrationState,
    registrationViewModel: RegistrationViewModel,
    onValueChange: (RegistrationState) -> Unit,
    onNext: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        InputField(
            input = input.firstName,
            onValueChange = {
                onValueChange(input.copy(firstName = it))
            },
            label = { Text(text = stringResource(R.string.first_name_label)) },
            modifier = Modifier
                .width(dimensionResource(R.dimen.text_field_width))
                .padding(bottom = 15.dp)
                .testTag("FirstName"),
            supportingText = {
                ErrorMessage(
                    input = input.firstName,
                    error = registrationViewModel.getRegistrationError().firstNameError,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
    Spacer(modifier = Modifier.padding(top = 5.dp))
    Text(text = stringResource(R.string.optional_text), modifier = Modifier.padding(start = 240.dp))
    input.prefixes?.let { prefixes ->
        InputField(
            input = prefixes,
            onValueChange = {
                onValueChange(input.copy(prefixes = it))
            },
            label = { Text(text = stringResource(R.string.prefixes_label)) },
            modifier = Modifier
                .width(dimensionResource(R.dimen.text_field_width))
                .testTag("Prefixes"),
            supportingText = {
                ErrorMessage(
                    input = input.prefixes,
                    error = registrationViewModel.getRegistrationError().prefixesError,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        )
    }
    Spacer(modifier = Modifier.padding(top = 40.dp))
    InputField(
        input = input.lastName,
        onValueChange = {
            onValueChange(input.copy(lastName = it))
        },
        label = { Text(text = stringResource(R.string.last_name_label)) },
        modifier = Modifier
            .width(dimensionResource(R.dimen.text_field_width))
            .testTag("LastName"),
        supportingText = {
            ErrorMessage(
                input = input.lastName,
                error = registrationViewModel.getRegistrationError().lastNameError,
            )
        }
    )
    ActionButton(
        buttonName = stringResource(R.string.next_button_text), onClick = { onNext() },
        enabled = registrationViewModel.validateFullName(),
        modifier = Modifier
            .width(290.dp)
            .padding(top = 20.dp)
    )
}

/**
 * UI element input field
 * @author Ömer Aynaci
 * @param input the input value
 * @param onValueChange input value changes
 * @param label the label of the input field
 * @param supportingText the supporting text for displaying error messages
 * @param modifier the modifier
 */
@Composable
private fun InputField(
    input: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit),
    supportingText: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
) {
    Column {
        Box(
            contentAlignment = Alignment.TopCenter,
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = onValueChange,
                label = label,
                modifier = modifier,
                singleLine = true,
                supportingText = supportingText
            )
        }
    }
}

/**
 * UI element to display the message if an error occurs
 * @author Ömer Aynaci
 * @param input the email value
 * @param error the error that is being occurred
 * @param modifier the modifier to style the element
 */
@Composable
fun ErrorMessage(
    input: String?,
    error: ValidationResult,
    modifier: Modifier = Modifier,
) {
    if (!input.isNullOrEmpty()) {
        Text(
            text = error.errorMessage,
            color = Color.Red,
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier,
        )
    }
}