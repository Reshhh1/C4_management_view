package com.example.frontend_android.teams.presentation.overview

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend_android.teams.data.repository.TeamRepository
import com.example.frontend_android.teams.presentation.overview.state.TeamOverViewState
import com.example.frontend_android.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamOverviewViewModel @Inject constructor(
    private val teamRepository: TeamRepository
) : ViewModel() {
    private val _viewModelState =
        mutableStateOf<TeamOverViewState>(TeamOverViewState.Loading)
    val teamState: State<TeamOverViewState> = _viewModelState

    /**
     * Fetches the team data when the view model is being instantiated
     * @author Reshwan Barhoe
     */
    init {
        getTeams()
    }

    /**
     * Fetches the team summaries and handles the result accordingly
     * @author Reshwan Barhoe
     */
    private fun getTeams() {
        viewModelScope.launch {
            when (val result = teamRepository.getTeamSummaries()) {
                is Resource.Success -> result.data?.let {
                    updateViewModelState(TeamOverViewState.Success(it))
                }

                is Resource.Error -> result.message?.let {
                    updateViewModelState(TeamOverViewState.Error(it))
                }
            }
        }
    }

    /**
     * Updates the view model state
     * @author Reshwan Barhoe
     * @param overViewState that's being assigned
     */
    private fun updateViewModelState(overViewState: TeamOverViewState) {
        _viewModelState.value = overViewState
    }

}