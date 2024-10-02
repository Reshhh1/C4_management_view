package com.example.frontend_android.auth.presentation.dashboard

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend_android.R
import com.example.frontend_android.auth.data.remote.dtos.response.AuthenticationMessage
import com.example.frontend_android.auth.data.remote.dtos.response.DashboardMessage
import com.example.frontend_android.auth.presentation.AuthenticatedUserState
import com.example.frontend_android.auth.presentation.login.LoginViewModel
import com.example.frontend_android.ui.widgets.CenteredLoadingIndicator

/**
 * Dashboard screen
 * @author Reshwan Barhoe
 * @param navigateToTeamOverview navigates the user to the team overview screen
 *
 */
@Composable
fun DashboardScreen(
    viewModel: LoginViewModel,
    navigateToTeamOverview: () -> Unit,
    navigateToC4OverView: () -> Unit
) {
    val userState by viewModel.userState.collectAsState()

    when (val currentState = userState) {
        is AuthenticatedUserState.Success -> {
            val user = currentState.user
            SuccessScreen(
                authenticationMessage = user,
                navigateToTeamOverview = navigateToTeamOverview,
                navigateToC4OverView = navigateToC4OverView
            )
        }

        is AuthenticatedUserState.Error -> {
            ErrorScreen()
        }

        is AuthenticatedUserState.Loading -> {
            CenteredLoadingIndicator()
        }
    }
}


/**
 * composable for when the user state is successful
 * @author Ömer Aynaci
 * @param authenticationMessage the authentication message after login
 * @param navigateToTeamOverview navigates the user to the team overview screen
 * @param navigateToC4OverView navigates to the C4 overview
 */
@SuppressLint("ResourceAsColor")
@Composable
private fun SuccessScreen(
    authenticationMessage: AuthenticationMessage?,
    navigateToTeamOverview: () -> Unit,
    navigateToC4OverView: () -> Unit
) {
    Column {
        if (authenticationMessage != null) {
            val dashboardMessage = DashboardMessage("Welcome, \n${authenticationMessage.message}")
            Text(
                text = dashboardMessage.message,
                modifier = Modifier
                    .padding(start = 35.dp, top = 40.dp)
                    .testTag("firstName"),
                fontSize = 28.sp,
                lineHeight = 30.sp
            )
            DashboardButton(
                buttonName = R.string.c4_model_title,
                topPadding = 60.dp,
                modifier = Modifier.padding(end = 150.dp),
                onClick = navigateToC4OverView
            )
            DashboardButton(
                buttonName = R.string.dashboard_teams_button,
                topPadding = 15.dp,
                modifier = Modifier.padding(end = 190.dp),
                onClick = navigateToTeamOverview
            )
        }
    }
}


/**
 * composable for a dashboard button
 * @author Ömer Aynaci
 * @param buttonName the button text
 * @param topPadding the padding for the top
 */
@Composable
private fun DashboardButton(
    @StringRes buttonName: Int,
    topPadding: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .padding(start = 20.dp, top = topPadding)
            .height(70.dp)
            .width(350.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(217, 217, 217),
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(5.dp)
    ) {
        Text(
            text = stringResource(buttonName),
            fontWeight = FontWeight.Normal,
            fontSize = 25.sp,
            modifier = modifier
        )
        Text(
            text = ">>", fontWeight = FontWeight.Normal,
            fontSize = 25.sp
        )
    }
}

/**
 * composable for when the user state is Error
 * @author Ömer Aynaci
 */
@Composable
private fun ErrorScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Something went wrong!")
    }
}