package com.example.frontend_android.c4_model.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.frontend_android.R
import com.example.frontend_android.c4_model.data.remote.dtos.ElementDTO
import com.example.frontend_android.c4_model.presentation.states.C4ElementState
import com.example.frontend_android.c4_model.presentation.states.C4ModelState
import com.example.frontend_android.c4_model.presentation.states.C4ModelUiState
import com.example.frontend_android.ui.components.OverviewRow
import com.example.frontend_android.ui.components.ShowError
import com.example.frontend_android.ui.components.wrappers.ContentWrapper
import com.example.frontend_android.ui.components.wrappers.WrapperActionIcon
import com.example.frontend_android.ui.widgets.CenteredLoadingIndicator

/**
 * C4 overview screen
 * @author Reshwan Barhoe
 * @param c4ModelUiState UI state of this screen
 * @param c4ModelState state of this screen
 * @param isRowExpanded checks if the provided row id is expanded
 * @param toggleElementExpansionById toggles the provided element id
 * @param getContainersByContextId gets the containers by the provided id
 * @param navigateToDashboard navigates to the dashboard
 */
@Composable
fun C4Screen(
    c4ModelUiState: C4ModelUiState,
    c4ModelState: C4ModelState,
    isRowExpanded: (id: String) -> Boolean,
    toggleElementExpansionById: (id: String) -> Unit,
    getContainersByContextId: (id: String) -> Unit,
    navigateToDashboard: () -> Unit
) {
    ContentWrapper(
        title = stringResource(id = R.string.c4_model_title),
        topLeftIcons = {
            WrapperActionIcon(
                onClick = navigateToDashboard,
                imageVector = Icons.Default.ArrowBackIosNew
            )
        },
    ) {
        TopSection()
        MainContent {
            when (c4ModelUiState) {
                is C4ModelUiState.Success -> C4ModelList(
                    c4ModelState = c4ModelState,
                    getContainersByContextId = getContainersByContextId,
                    toggleElementExpansionById = toggleElementExpansionById,
                    isRowExpanded = isRowExpanded
                )
                is C4ModelUiState.Loading -> CenteredLoadingIndicator()
                is C4ModelUiState.Error -> ShowError(message = c4ModelUiState.errorMessage)
            }
        }
    }
}

/**
 * The top section of the screen
 * [The searchbar will be implemented here]
 * @author Reshwan Barhoe
 */
@Composable
fun TopSection() {
    Row(modifier = Modifier.padding(25.dp)) {}
}

/**
 * Main content
 * @author Reshwan Barhoe
 * @param content being passed into
 */
@Composable
fun MainContent(content: @Composable () -> Unit) {
    Column {
        content()
    }
}

/**
 * List of contexts
 * @author Reshwan Barhoe
 * @param c4ModelState state of the c4model
 * @param isRowExpanded checks if the provided row id is expanded
 * @param toggleElementExpansionById toggles the provided element id
 * @param getContainersByContextId gets the containers by the provided id
 */
@Composable
fun C4ModelList(
    c4ModelState: C4ModelState,
    isRowExpanded: (id: String) -> Boolean,
    toggleElementExpansionById: (id: String) -> Unit,
    getContainersByContextId: (id: String) -> Unit
) {
    val contexts = c4ModelState.contexts
    if (contexts.isEmpty()) {
        ShowError(message = stringResource(id = R.string.c4_no_contexts))
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            items(contexts) {
                C4ModelLayout(
                    c4ModelState = c4ModelState,
                    contextInfo = it.elementInformation,
                    toggleElementExpansionById = { toggleElementExpansionById(it.id) },
                    getContainersByContextId = { getContainersByContextId(it.id) },
                    isRowExpanded = isRowExpanded
                )
            }
        }
    }
}

/**
 * Layout of the c4 layers
 * @author Reshwan Barhoe
 * @param c4ModelState state of the c4model
 * @param isRowExpanded checks if the provided row id is expanded
 * @param toggleElementExpansionById toggles the provided element id
 * @param getContainersByContextId gets the containers by the provided id
 */
@Composable
private fun C4ModelLayout(
    c4ModelState: C4ModelState,
    contextInfo: ElementDTO,
    toggleElementExpansionById: () -> Unit,
    getContainersByContextId: () -> Unit,
    isRowExpanded: (id: String) -> Boolean
) {
    ContextPreview(
        contextInfo = contextInfo,
        toggleElementExpansionById = toggleElementExpansionById,
        getContainersByContextId = getContainersByContextId,
        isRowExpanded = isRowExpanded
    )
    if (isRowExpanded(contextInfo.id)) {
        ContainerPreview(
            contextId = contextInfo.contextId,
            containers = c4ModelState.containers
        )
    }
}

/**
 * Context preview
 * @author Reshwan Barhoe
 * @param contextInfo information of the current context
 * @param toggleElementExpansionById toggles the provided element id
 * @param isRowExpanded checks if the provided row id is expanded
 * @param getContainersByContextId gets the containers by the provided id
 */
@Composable
private fun ContextPreview(
    contextInfo: ElementDTO,
    isRowExpanded: (id: String) -> Boolean,
    toggleElementExpansionById: () -> Unit,
    getContainersByContextId: () -> Unit
) {
    OverviewRow(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                toggleElementExpansionById()
                if (isRowExpanded(contextInfo.id)) {
                    getContainersByContextId()
                }
            },
        isExpanded = isRowExpanded(contextInfo.id),
        maxTextWidth = dimensionResource(id = R.dimen.c1_overview_padding),
        title = contextInfo.name,
        painter = painterResource(id = R.drawable.c1)
    )
}

/**
 * Container preview
 * @author Reshwan Barhoe
 * @param contextId parent id of the container
 * @param containers list of containers
 */
@Composable
private fun ContainerPreview(
    contextId: String,
    containers: List<C4ElementState>,
) {
    containers.forEach {
        Row(
            modifier = Modifier
                .clickable { }
        ) {
            if (it.contextId == contextId) {
                OverviewRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    maxTextWidth = dimensionResource(id = R.dimen.c2_overview_padding),
                    painter = painterResource(id = R.drawable.c2),
                    title = it.elementInformation.name
                )
            }
        }
    }
}