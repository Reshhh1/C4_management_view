package com.example.frontend_android.ui.components.wrappers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Standard wrapper for forms
 * @author Reshwan Barhoe
 * @param title that's being displayed
 * @param topLeftIcons composable icons
 * @param topRightIcons composable icons
 * @param content that's being wrapped by this wrapper
 */
@Composable
fun ContentWrapper(
    title: String,
    topLeftIcons: @Composable () -> Unit = {},
    topRightIcons: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            TopBar(
                title = title,
                topLeftIcons = topLeftIcons,
                topRightIcons = topRightIcons
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
        ) {
            content()
        }
    }
}

/**
 * Topbar that's being used to make the wrapper
 * @param title that's being displayed
 * @param topLeftIcons composable icons
 * @param topRightIcons composable icons
 * @author Reshwan Barhoe
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    title: String,
    topLeftIcons: @Composable () -> Unit = {},
    topRightIcons: @Composable () -> Unit = {}
) {
    TopAppBar(
        modifier = Modifier
            .shadow(5.dp),
        title = { ActionBarTitle(title) },
        navigationIcon = {
            topLeftIcons()
        },
        actions = {
            topRightIcons()
        }
    )
}

/**
 * Title of the actionbar
 * @author Reshwan Barhoe
 * @param title that's being displayed
 */
@Composable
private fun ActionBarTitle(title: String) {
    Text(
        modifier = Modifier.padding(start = 10.dp),
        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        ),
        text = title
    )
}

/**
 * Action icon that's being displayed in the action bar
 * @author Reshwan Barhoe
 * @param onClick executes if the icon is clicked
 * @param imageVector icon that's being displayed
 * @param description of the icon
 */
@Composable
fun WrapperActionIcon(
    onClick: () -> Unit,
    imageVector: ImageVector = Icons.Default.Close,
    description: String = ""
) {
    IconButton(onClick = { onClick() }) {
        Icon(
            imageVector = imageVector,
            contentDescription = description
        )
    }
}