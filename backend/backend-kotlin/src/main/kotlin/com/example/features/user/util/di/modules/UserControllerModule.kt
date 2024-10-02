package com.example.features.user.util.di.modules

import com.example.features.user.business.service.*
import com.example.features.user.controller.*
import com.example.general.exception.*
import dagger.*

@Module
class UserControllerModule {

    /**
     * Provides an instance of the user controller
     * @author Reshwan Barhoe
     * @param userService needed dependency
     * @param exceptionHandler needed dependency
     * @return an instance of UserController
     */
    @Provides
    fun provideUserController(
        userService: UserService,
        exceptionHandler: ExceptionHandler
    ): UserController {
        return UserController(
            userService = userService,
            exceptionHandler = exceptionHandler
        )
    }
}