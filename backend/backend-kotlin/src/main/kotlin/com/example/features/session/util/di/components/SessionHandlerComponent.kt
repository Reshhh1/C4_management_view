package com.example.features.session.util.di.components

import com.example.features.session.business.service.SessionService
import com.example.features.session.controller.SessionController
import com.example.features.session.util.di.modules.SessionControllerModule
import com.example.features.session.util.di.modules.SessionRepositoryModule
import com.example.features.session.util.di.modules.SessionServiceModule
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
        SessionControllerModule::class,
        SessionServiceModule::class,
        SessionRepositoryModule::class,
        UserRepositoryModule::class,
        UserFactoryModule::class,
        ExceptionHandlerModule::class
    ]
)
interface SessionHandlerComponent {
    fun getSessionController(): SessionController
    fun getSessionService(): SessionService
}