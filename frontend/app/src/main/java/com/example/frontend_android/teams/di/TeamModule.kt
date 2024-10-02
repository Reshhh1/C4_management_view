package com.example.frontend_android.teams.di

import android.content.Context
import com.example.frontend_android.auth.data.local.getToken
import com.example.frontend_android.auth.data.remote.network.authorizationHeader
import com.example.frontend_android.teams.data.remote.TeamApi
import com.example.frontend_android.teams.data.repository.TeamImplementation
import com.example.frontend_android.teams.data.repository.TeamRepository
import com.example.frontend_android.teams.domain.use_cases.CreateTeamUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TeamModule {

    /**
     * Provides the teamRepository
     * @param teamApi the required dependency
     * @author Reshwan Barhoe
     * @return TeamRepository
     */
    @Provides
    @Singleton
    fun provideTeamRepository(
        teamApi: TeamApi
    ): TeamRepository {
        return TeamImplementation(teamApi)
    }

    /**
     * Provides the CreateTeamUseCase
     * @param teamRepository the required dependency
     * @author Reshwan Barhoe
     * @return TeamRepository
     */
    @Provides
    @Singleton
    fun provideCreateTeamUseCase(
        teamRepository: TeamRepository
    ): CreateTeamUseCase {
        return CreateTeamUseCase(
            teamRepository = teamRepository
        )
    }


    /**
     * Provides the teamApi
     * @param context the required dependency
     * @author Reshwan Barhoe
     * @return TeamApi
     */
    @Provides
    @Singleton
    fun provideTeamApi(@ApplicationContext context: Context): TeamApi {
        return authorizationHeader(getToken(context)).create(TeamApi::class.java)
    }
}