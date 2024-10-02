package com.example.frontend_android.teams.di

import com.example.frontend_android.teams.data.repository.TeamRepository
import com.example.frontend_android.teams.domain.use_cases.CreateTeamUseCase
import com.example.frontend_android.teams.presentation.create.TeamCreateViewModel
import com.example.frontend_android.teams.presentation.overview.TeamOverviewViewModel
import com.example.frontend_android.users.domain.use_cases.SearchUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class TeamViewModelModule {

    /**
     * Provides the TeamOverView viewModel
     * @author Reshwan Barhoe
     * @param teamRepository needed dependency
     * @return TeamOverViewViewModel
     */
    @Provides
    @ViewModelScoped
    fun provideTeamOverviewModule(
        teamRepository: TeamRepository
    ): TeamOverviewViewModel {
        return TeamOverviewViewModel(teamRepository)
    }

    /**
     * Provides the TeamCreate viewModel
     * @author Reshwan Barhoe
     * @param createTeamUseCase needed dependency
     * @param searchUserUseCase needed dependency
     * @return TeamCreateViewModel
     */
    @Provides
    @ViewModelScoped
    fun provideTeamCreateModule(
        createTeamUseCase: CreateTeamUseCase,
        searchUserUseCase: SearchUserUseCase
    ): TeamCreateViewModel {
        return TeamCreateViewModel(
            createTeamUseCase,
            searchUserUseCase
        )
    }
}