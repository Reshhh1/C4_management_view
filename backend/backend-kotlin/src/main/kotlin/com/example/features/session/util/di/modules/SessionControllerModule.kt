package com.example.features.session.util.di.modules

import com.example.features.session.business.service.*
import com.example.features.session.controller.*
import com.example.general.exception.*
import dagger.*

@Module
class SessionControllerModule {

    /**
     * Provides an instance of the session controller
     * @author Reshwan Barhoe
     * @param sessionService needed dependency
     * @param exceptionHandler needed dependency
     * @return an instance of SessionController
     */
    @Provides
    fun provideSessionController(
        sessionService: SessionService,
        exceptionHandler: ExceptionHandler
    ): SessionController {
        return SessionController(
            sessionService = sessionService,
            exceptionHandler = exceptionHandler
        )
    }
}