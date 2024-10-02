package com.example.features.team.util.di.modules

import com.example.features.team.business.service.TeamService
import com.example.features.team.data.repository.TeamRepository
import com.example.features.user.data.repository.UserRepository
import com.example.util.converters.TeamFactory
import dagger.Module
import dagger.Provides

@Module
class TeamServiceModule {

    /**
     * Provides an instance of the team service
     * @author Reshwan Barhoe
     * @param teamRepository needed dependency
     * @param teamFactory needed dependency
     * @return an instance of the TeamService
     *
     */
    @Provides
    fun provideTeamService(
        teamRepository: TeamRepository,
        userRepository: UserRepository,
        teamFactory: TeamFactory
    ): TeamService {
        return TeamService(
            teamRepository = teamRepository,
            userRepository = userRepository,
            teamFactory = teamFactory
        )
    }
}