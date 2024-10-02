package com.example.features.session.util.di.modules

import com.example.features.session.business.service.*
import com.example.features.session.data.repository.*
import com.example.features.user.data.repository.*
import dagger.*

@Module
class SessionServiceModule {

    /**
     * Provides an instance of the session service
     * @author Reshwan Barhoe
     * @param sessionRepository needed dependency
     * @param userRepository needed dependency
     * @return an instance of SessionService
     */
    @Provides
    fun provideSessionService(
        sessionRepository: SessionRepository,
        userRepository: UserRepository,
    ): SessionService {
        return SessionService(
            sessionRepository = sessionRepository,
            userRepository = userRepository,
        )
    }
}