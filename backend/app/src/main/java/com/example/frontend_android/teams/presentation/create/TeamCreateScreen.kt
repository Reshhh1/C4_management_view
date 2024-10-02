package com.example.frontend_android.teams.presentation.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend_android.R
import com.example.frontend_android.teams.presentation.create.state.TeamCreateState
import com.example.frontend_android.ui.components.FieldError
import com.example.frontend_android.ui.components.InputField
import com.example.frontend_android.ui.components.wrappers.ContentWrapper
import com.example.frontend_android.ui.components.wrappers.WrapperActionIcon
import com.example.frontend_android.users.data.remote.dtos.UserSummary

/**
 * Main screen of the CreateTeam
 * @author Reshwan Barhoe
 * @param state of this screen
 * @param networkError that's occurring
 * @param onSubmit executes upon creating a team
 * @param navigateToTeamOverView navigates the client to TeamOverView
 * @param onTeamNameChange executes and updates the input field
 */
@Composable
fun TeamCreateScreen(
    state: TeamCreateState,
    networkError: String,
    onSubmit: () -> Unit,
    onMemberRemove: (member: UserSummary) -> Unit,
    navigateToTeamOverView: () -> Unit = {},
    navigateToAddMember: () -> Unit = {},
    onTeamNameChange: (newValue: TeamCreateState) -> Unit,
    getUsersByName: () -> Unit = {},
    resetState: () -> Unit = {}
) {
    if (state.successfullyCreated) {
        navigateToTeamOverView()
        resetState()
    }
    ContentWrapper(
        title = stringResource(id = R.string.actionbar_team, "Create"),

        topLeftIcons = {
            WrapperActionIcon(
                imageVector = Icons.Default.Close,
                onClick = navigateToTeamOverView
            )
        },
        topRightIcons = {
            WrapperActionIcon(
                imageVector = Icons.Default.Check,
                onClick = onSubmit,
                description = "Save"
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            if (networkError.isNotEmpty()) {
                Text(text = networkError)
            }
            Form(
                state = state,
                onTeamNameChange = onTeamNameChange,
                navigateToAddMember = navigateToAddMember,
                getUsersByName = getUsersByName,
                onMemberRemove = onMemberRemove
            )
        }
    }
}

/**
 * Form of the create team screen
 * @author Reshwan Barhoe
 * @param state of this screen
 * @param onTeamNameChange executes and updates the input field
 */
@Composable
private fun Form(
    state: TeamCreateState,
    onTeamNameChange: (newValue: TeamCreateState) -> Unit,
    navigateToAddMember: () -> Unit = {},
    getUsersByName: () -> Unit,
    onMemberRemove: (member: UserSummary) -> Unit
) {
    TeamNameSection(
        state = state,
        onTeamNameChange = onTeamNameChange
    )
    TeamMemberSection(
        state = state,
        navigateToAddMember = navigateToAddMember,
        getUsersByName = getUsersByName,
        onMemberRemove = onMemberRemove
    )
}

/**
 * Team name section
 * @author Reshwan Barhoe
 * @param state of this screen
 * @param onTeamNameChange executes upon name change
 */
@Composable
fun TeamNameSection(
    state: TeamCreateState,
    onTeamNameChange: (newValue: TeamCreateState) -> Unit
) {
    FormRow(
        icon = {
            Icon(
                imageVector = Icons.Outlined.Create,
                contentDescription = "Pencil icon"
            )
        }
    ) {
        InputField(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_team-name"),
            placeHolder = {
                Text(
                    text = stringResource(
                        id = R.string.input_placeholder, "team name"
                    )
                )
            },
            value = state.name,
            onValueChange = { onTeamNameChange(state.copy(name = it)) },
            isError = (state.nameError.isNotEmpty()),
            supportingText = { FieldError(errorMessage = state.nameError) }
        )
    }
}

/**
 * Team member section
 * @author Reshwan Barhoe
 * @param state of this screen
 * @param navigateToAddMember navigates to the add member screen
 * @param getUsersByName gets a list of users
 * @param onMemberRemove executes upon removing a member
 */
@Composable
fun TeamMemberSection(
    state: TeamCreateState,
    navigateToAddMember: () -> Unit,
    getUsersByName: () -> Unit,
    onMemberRemove: (member: UserSummary) -> Unit
) {
    FormRow(
        modifier = Modifier.clickable {
            navigateToAddMember()
            getUsersByName()
        },
        icon = {
            Icon(
                painter = painterResource(R.drawable.person_add),
                contentDescription = "Add member"
            )
        }
    ) {
        AddMember()
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.LightGray
        )
    }
    FormRow(
        modifier = Modifier.padding(
            start = 40.dp,
            top = dimensionResource(id = R.dimen.small_dimen)
        )
    ) {
        FieldError(
            errorMessage = state.memberError,
            textStyle = TextStyle(fontSize = 13.sp)
        )
    }
    FormRow {
        MemberList(
            members = state.members,
            onMemberRemove = onMemberRemove
        )
    }
}

/**
 * List of selected members
 * @author Reshwan Barhoe
 * @param members that are being displayed
 * @param onMemberRemove removes a selected member
 */
@Composable
fun MemberList(
    members: List<UserSummary>,
    onMemberRemove: (member: UserSummary) -> Unit
) {
    members.forEach {
        val fullName = "${it.firstName} ${it.prefixes} ${it.lastName}".trim()
        val fullNameWithoutExtraSpaces = fullName.replace("\\s+".toRegex(), " ")

        FormRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.small_dimen)),
            icon = {
                Icon(
                    modifier = Modifier
                        .size(45.dp)
                        .fillMaxSize(),
                    tint = Color.DarkGray,
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Member icon"
                )
            }) {
            Row(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    fontSize = 15.sp,
                    text = fullNameWithoutExtraSpaces
                )
                Icon(
                    modifier = Modifier.clickable { onMemberRemove(it) },
                    imageVector = Icons.Default.Close,
                    contentDescription = "deselect member"
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.LightGray
        )
    }
}

/**
 * Add member section
 * @author Reshwan Barhoe
 */
@Composable
fun AddMember() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = stringResource(R.string.team_add_members),
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * Row which is being used in the form of this screen
 * @author Reshwan Barhoe
 * @param modifier for modification
 * @param icon of the form row
 * @param content that's being displayed on the right side
 */
@Composable
fun FormRow(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.padding(start = 20.dp, end = 30.dp)) {
            icon()
        }
        Column {
            content()
        }
    }
}
