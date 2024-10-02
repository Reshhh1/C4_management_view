package com.example.features.team.util.di.modules

import com.example.features.team.business.service.TeamService
import com.example.features.team.controller.TeamController
import com.example.general.exception.ExceptionHandler
import dagger.Module
import dagger.Provides

@Module
class TeamControllerModule {

    /**
     * Provides an instance of the team controller
     * @author Reshwan Barhoe
     * @param teamService needed dependency
     * @param exceptionHandler needed dependency
     * @return an instance of the TeamController
     *
     */
    @Provides
    fun provideTeamController(
        teamService: TeamService,
        exceptionHandler: ExceptionHandler
    ): TeamController {
        return TeamController(
            teamService = teamService,
            exceptionHandler = exceptionHandler
        )
    }
}