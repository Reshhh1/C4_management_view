package com.example.features.team.util.di.modules

import com.example.features.team.data.repository.TeamRepository
import com.example.util.converters.TeamFactory
import com.example.util.converters.UserFactory
import dagger.Module
import dagger.Provides

@Module
class TeamRepositoryModule {

    /**
     * Provides an instance of the team repository
     * @param teamFactory dependency that's being provided
     * @param userFactory dependency that's being provided
     * @author Reshwan Barhoe
     * @return an instance of the TeamRepository
     */
    @Provides
    fun provideTeamRepository(teamFactory: TeamFactory, userFactory: UserFactory): TeamRepository {
        return TeamRepository(teamFactory, userFactory)
    }
}