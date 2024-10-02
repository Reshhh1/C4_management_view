package com.example.features.user.util.di.components

import com.example.features.user.business.service.UserService
import com.example.features.user.controller.UserController
import com.example.features.user.data.repository.UserRepository
import com.example.features.user.util.di.modules.UserControllerModule
import com.example.features.user.util.di.modules.UserFactoryModule
import com.example.features.user.util.di.modules.UserRepositoryModule
import com.example.features.user.util.di.modules.UserServiceModule
import com.example.general.di.modules.ExceptionHandlerModule
import com.example.general.di.modules.SearchTermModule
import dagger.Component
import javax.inject.Singleton

/**
 * Provides the needed dependencies
 * @author Reshwan Barhoe
 */
@Singleton
@Component(
    modules = [
        UserControllerModule::class,
        UserServiceModule::class,
        UserRepositoryModule::class,
        UserFactoryModule::class,
        ExceptionHandlerModule::class,
        SearchTermModule::class
    ]
)
interface UserHandlerComponent {
    fun getUserController(): UserController
    fun getUserService(): UserService

    fun getUserRepository(): UserRepository
}