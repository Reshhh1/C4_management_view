package com.example.features.team.util.di.modules

import com.example.util.converters.TeamFactory
import dagger.Module
import dagger.Provides

@Module
class TeamFactoryModule {

    /**
     * Provides an instance of the team factory
     * @author Reshwan Barhoe
     * @return an instance of the TeamFactory
     */
    @Provides
    fun providesTeamFactory(): TeamFactory {
        return TeamFactory()
    }
}