package com.example.frontend_android.teams.presentation.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.frontend_android.R
import com.example.frontend_android.teams.presentation.create.state.TeamCreateState
import com.example.frontend_android.teams.presentation.create.state.TeamUserListState
import com.example.frontend_android.ui.components.FieldError
import com.example.frontend_android.ui.components.InputField
import com.example.frontend_android.ui.components.wrappers.ContentWrapper
import com.example.frontend_android.ui.components.wrappers.WrapperActionIcon
import com.example.frontend_android.ui.widgets.CenteredLoadingIndicator
import com.example.frontend_android.users.data.remote.dtos.UserSummary
import com.example.frontend_android.util.extensions.removeExtraSpacing


/**
 * Main screen of the Team create add member
 * @author Reshwan Barhoe
 * @param createTeamCreateState state of this screen
 * @param userListState state of the user list
 * @param onUserSearchTermChange updates the search term state
 * @param addSelectedMember adds a selected member
 * @param removeSelectedMember removes a selected member
 * @param saveSelectedMembers saves the selected members
 * @param cancelSelectedMembers cancels the selected members
 * @param navigateToTeamCreate navigates to TeamCreate
 * @param networkError network errors
 */
@Composable
fun TeamCreateAddMemberScreen(
    createTeamCreateState: TeamCreateState,
    userListState: TeamUserListState,
    onUserSearchTermChange: (term: String) -> Unit,
    addSelectedMember: (user: UserSummary) -> Unit,
    removeSelectedMember: (user: UserSummary) -> Unit,
    isSelectedMembersValid: () -> Boolean,
    saveSelectedMembers: () -> Unit,
    cancelSelectedMembers: () -> Unit,
    navigateToTeamCreate: () -> Unit,
    networkError: String
) {
    ContentWrapper(
        title = stringResource(id = R.string.team_add_members),
        topLeftIcons = {
            WrapperActionIcon(
                imageVector = Icons.Default.Close,
                onClick = {
                    navigateToTeamCreate()
                    cancelSelectedMembers()
                }
            )
        },
        topRightIcons = {
            WrapperActionIcon(
                imageVector = Icons.Default.Check,
                onClick = {
                    if (isSelectedMembersValid()) {
                        navigateToTeamCreate()
                        saveSelectedMembers()
                    }
                },
                description = "Save"
            )
        }
    ) {
        MainContent(
            onUserSearchTermChange = onUserSearchTermChange,
            createTeamCreateState = createTeamCreateState,
            userListState = userListState,
            addSelectedMember = addSelectedMember,
            removeSelectedMember = removeSelectedMember,
            networkError = networkError
        )
    }
}

/**
 * Main content of this screen
 * @author Reshwan Barhoe
 * @param createTeamCreateState state of this screen
 * @param userListState state of the user list
 * @param onUserSearchTermChange updates the search term state
 * @param removeSelectedMember removes a selected member
 * @param networkError network errors
 */
@Composable
fun MainContent(
    onUserSearchTermChange: (term: String) -> Unit,
    createTeamCreateState: TeamCreateState,
    userListState: TeamUserListState,
    addSelectedMember: (user: UserSummary) -> Unit,
    removeSelectedMember: (user: UserSummary) -> Unit,
    networkError: String
) {
    Column {
        UserSearchBar(
            onUserSearchTermChange = onUserSearchTermChange,
            createTeamCreateState = createTeamCreateState
        )
        SelectedMembers(
            removeSelectedMember = removeSelectedMember,
            selectedMembers = createTeamCreateState.selectedMembers
        )
        UserListContainer(
            createTeamCreateState = createTeamCreateState,
            userListState = userListState,
            addSelectedMember = addSelectedMember,
            networkError = networkError
        )
    }
}

/**
 * Container that acts depending on the user list state
 * @author Reshwan Barhoe
 * @param createTeamCreateState state of this screen
 * @param userListState state of the user list
 * @param networkError network errors
 */
@Composable
fun UserListContainer(
    createTeamCreateState: TeamCreateState,
    userListState: TeamUserListState,
    addSelectedMember: (user: UserSummary) -> Unit,
    networkError: String
) {
    when (userListState) {
        is TeamUserListState.Success -> {
            UserList(
                addSelectedMember = addSelectedMember,
                users = userListState.users.filter {
                    !createTeamCreateState.selectedMembers.any { selectedMember ->
                        selectedMember.id == it.id
                    }
                }
            )
        }

        is TeamUserListState.Loading -> {
            CenteredLoadingIndicator()
        }

        is TeamUserListState.Error -> {
            FieldError(errorMessage = networkError)
        }
    }
}

/**
 * Searchbar for searching users
 * @author Reshwan Barhoe
 * @param createTeamCreateState state of this screen
 * @param onUserSearchTermChange updates the search term state
 */
@Composable
fun UserSearchBar(
    onUserSearchTermChange: (term: String) -> Unit,
    createTeamCreateState: TeamCreateState
) {
    FormRow(icon = {
        Icon(
            painter = painterResource(id = R.drawable.person_add),
            contentDescription = "Add member"
        )
    }) {
        InputField(
            modifier = Modifier.fillMaxWidth(),
            placeHolder = {
                Text(
                    text = stringResource(
                        id = R.string.user_search
                    )
                )
            },
            onValueChange = onUserSearchTermChange,
            value = createTeamCreateState.userSearchTerm,
            supportingText = { FieldError(errorMessage = createTeamCreateState.selectedMemberError) }
        )
    }
}

/**
 * Separated composable for the selected members
 * @param removeSelectedMember removes a selected member
 * @param selectedMembers list of selected members
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectedMembers(
    removeSelectedMember: (user: UserSummary) -> Unit,
    selectedMembers: List<UserSummary>
) {
    Column {
        FlowRow(Modifier.padding(horizontal = dimensionResource(id = R.dimen.small_dimen))) {
            if (selectedMembers.isNotEmpty()) {
                selectedMembers.forEach {
                    InputChip(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        selected = true,
                        onClick = { removeSelectedMember(it) },
                        label = { Text("${it.firstName} ${it.prefixes} ${it.lastName}".removeExtraSpacing()) },
                        trailingIcon = {
                            Icon(
                                modifier = Modifier.size(15.dp),
                                tint = Color.Black,
                                imageVector = Icons.Default.Close,
                                contentDescription = "close"
                            )
                        }
                    )
                }
            }
        }
    }
}


/**
 * A list of users
 * @author Reshwan Barhoe
 * @param users list of users that are being displayed
 * @param addSelectedMember adds a selected member
 */
@Composable
fun UserList(
    users: List<UserSummary>,
    addSelectedMember: (user: UserSummary) -> Unit

) {
    LazyColumn(
        modifier = Modifier
            .height(550.dp)
    ) {
        items(
            items = users
        ) {
            UserItem(
                userSummary = it,
                addSelectedMember = addSelectedMember
            )
        }
    }
}

/**
 * A user item that's being displayed inside of the list
 * @author Reshwan Barhoe
 * @param userSummary summary of the user
 * @param addSelectedMember add a selected member
 */
@Composable
fun UserItem(
    userSummary: UserSummary,
    addSelectedMember: (user: UserSummary) -> Unit
) {
    FormRow(
        modifier = Modifier
            .clickable { addSelectedMember(userSummary) },
        icon = {
            Icon(
                modifier = Modifier.size(65.dp),
                imageVector = Icons.Sharp.AccountCircle,
                contentDescription = "User profile"
            )
        }
    ) {
        Text(text = "${userSummary.firstName} ${userSummary.prefixes} ${userSummary.lastName}".removeExtraSpacing())
    }
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = Color.LightGray
    )
}
