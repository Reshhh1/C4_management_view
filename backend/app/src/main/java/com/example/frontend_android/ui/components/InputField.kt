package com.example.frontend_android.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp

/**
 * Inputfield that's being used through the whole application
 * @author Reshwan Barhoe
 * @param modifier for modifications
 * @param value that's being shown inside the inputfield
 * @param onValueChange executes upon changing the input value
 * @param placeHolder that's being displayed in the inputfield
 * @param isError highlights the field in red
 * @param supportingText used to display errors or to indicate optional fields
 * @param imeAction action that's being displayed on the keyboard
 * @param keyboardType keyboard type of the inputfield
 */
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (newValue: String) -> Unit = {},
    placeHolder: @Composable () -> Unit,
    isError: Boolean = false,
    supportingText: @Composable () -> Unit = {},
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    TextField(
        modifier = modifier,
        placeholder = { placeHolder() },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.LightGray,
            errorContainerColor = Color.Transparent
        ),
        textStyle = TextStyle(
            fontSize = 15.sp
        ),
        supportingText = { supportingText() },
        maxLines = 1,
        isError = isError,
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = keyboardType
        ),
        value = value,
        onValueChange = { onValueChange(it) }
    )
}