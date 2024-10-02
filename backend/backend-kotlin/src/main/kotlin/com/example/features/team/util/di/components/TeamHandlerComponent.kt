package com.example.features.team.util.di.components

import com.example.features.team.business.service.TeamService
import com.example.features.team.controller.TeamController
import com.example.features.team.data.repository.TeamRepository
import com.example.features.team.util.di.modules.TeamControllerModule
import com.example.features.team.util.di.modules.TeamFactoryModule
import com.example.features.team.util.di.modules.TeamRepositoryModule
import com.example.features.team.util.di.modules.TeamServiceModule
import com.example.features.user.util.di.modules.UserFactoryModule
import com.example.features.user.util.di.modules.UserRepositoryModule
import com.example.general.di.modules.ExceptionHandlerModule
import dagger.Component
import javax.inject.Singleton

/**
 * Provides the needed dependencies
 * @author Reshwan Barhoe
 */
@Singleton
@Component(
    modules = [
        TeamControllerModule::class,
        TeamServiceModule::class,
        TeamRepositoryModule::class,
        UserFactoryModule::class,
        UserRepositoryModule::class,
        ExceptionHandlerModule::class,
        TeamFactoryModule::class
    ]
)
interface TeamHandlerComponent {
    fun getTeamController(): TeamController
    fun getTeamService(): TeamService
    fun getTeamRepository(): TeamRepository
}