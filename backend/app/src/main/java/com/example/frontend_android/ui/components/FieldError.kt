package com.example.frontend_android.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

/**
 * Standard error display composable
 * used for supporting text & temporary error display
 * @author Reshwan Barhoe
 * @param errorMessage that's being displayed
 * @param textStyle Styling of the text
 */
@Composable
fun FieldError(
    errorMessage: String,
    textStyle: TextStyle = TextStyle(color = Color.Red)
) {
    Text(
        style = textStyle,
        color = Color.Red,
        text = errorMessage
    )
}