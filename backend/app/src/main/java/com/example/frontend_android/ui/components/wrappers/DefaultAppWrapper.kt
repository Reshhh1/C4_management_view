package com.example.frontend_android.ui.components.wrappers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

/**
 * A Composable function of the app
 * @author Ömer Aynaci / Reshwan Barhoe
 */
@Composable
fun ApplicationWrapper(content: @Composable () -> Unit) {
    Scaffold(
        topBar = {
            UserTopAppBar()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            content()
        }
    }
}

/**
 * A Composable function for displaying the top app bar
 *
 * @author Ömer Aynaci / Reshwan Barhoe
 * @param modifier Optional modifier for customizing the appearance and layout of the centered aligned top app bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun UserTopAppBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = "Management View") },
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.DarkGray,
            titleContentColor = Color.White
        )
    )
}