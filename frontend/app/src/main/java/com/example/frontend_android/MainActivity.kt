package com.example.frontend_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.frontend_android.ui.components.wrappers.ApplicationWrapper
import com.example.frontend_android.ui.navigation.AppNavigation
import com.example.frontend_android.ui.theme.FrontendandroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * this function is called when the application is being launched.
     * Composes the given composable into the given activity. The content will become the root view of the given activity.
     * @author Ömer Aynaci
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle
     * contains the data it most recently supplied in onSaveInstanceState. Note: Otherwise it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FrontendandroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ApplicationWrapper {
                        AppNavigation()
                    }
                }
            }
        }
    }
}