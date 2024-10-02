package com.example.frontend_android.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend_android.R
import com.example.frontend_android.ui.theme.Primary

/**
 * Label that is being used for input fields.
 * @author Reshwan Barhoe
 * @param labelId The placeholder that's being shown
 */
@Composable
fun LabelText(labelId: String) {
    Text(
        text = labelId,
        fontSize = 12.sp
    )
}

/**
 * Action button that's being used for button's that
 * require further action. This is made to keep the styling of
 * the application consistent
 * @author Reshwan Barhoe
 * @param modifier Optional modifier for styling
 * @param buttonName The button text that's being displayed
 * @param onClick The callback that's being executed on click
 */
@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    buttonName: String,
    enabled: Boolean = true,
    containerColor: ButtonColors = ButtonDefaults.buttonColors(containerColor = Primary),
    textColor: Color = Color.White,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = { onClick() },
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = dimensionResource(id = R.dimen.elevation_card)
        ),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onBackground
        ),
        modifier = modifier
    ) {
        Text(text = buttonName, color = textColor)
    }
}

/**
 * Composable that could be used to display
 * previews inside a column
 * @author Reshwan Barhoe
 * @param modifier modifies the composable
 * @param title being displayed
 */
@Composable
fun OverviewRow(
    modifier: Modifier = Modifier,
    title: String,
    maxTextWidth: Dp = dimensionResource(id = R.dimen.default_overview_text_padding),
    isExpanded: Boolean = false,
    painter: Painter = painterResource(id = R.drawable.default_team)
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        Image(
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.default_overview_icon_size))
                .align(Alignment.CenterVertically),
            painter = painter,
            contentDescription = "Unfold team"
        )
        Text(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.default_overview_padding))
                .width(maxTextWidth),
            text = title,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Icon(
            modifier = Modifier
                .padding(end = dimensionResource(id = R.dimen.default_overview_icon_padding))
                .align(Alignment.CenterVertically),
            imageVector = if (isExpanded) Icons.Sharp.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = "Unfold team"
        )
    }
}

/**
 * Displays a error with the provided message
 * @author Reshwan Barhoe
 * @param message that's being displayed
 */
@Composable
fun ShowError(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
            .padding(50.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * A button to navigate back
 * @author Ömer Aynaci
 * @param goBack
 */
@Composable
fun BackArrowButton(goBack: () -> Unit) {
    IconButton(onClick = { goBack() }) {
        Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "arrow back")
    }
}
