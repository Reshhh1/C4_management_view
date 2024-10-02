package com.example.features.session.util.di.modules

import com.example.features.session.data.repository.*
import dagger.*

@Module
class SessionRepositoryModule {

    /**
     * Provides an instance of the session repository
     * @author Reshwan Barhoe
     * @return an instance of SessionRepository
     */
    @Provides
    fun provideSessionRepository(): SessionRepository {
        return SessionRepository()
    }
}