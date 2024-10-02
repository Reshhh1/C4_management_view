package com.example.frontend_android.auth.di

import android.content.Context
import com.example.frontend_android.auth.data.local.getToken
import com.example.frontend_android.auth.data.remote.SessionApi
import com.example.frontend_android.auth.data.remote.network.authorizationHeader
import com.example.frontend_android.auth.data.repository.SessionImplementation
import com.example.frontend_android.auth.data.repository.SessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SessionModule {

    /**
     * providing the session repository
     * @author Ömer Aynaci
     * @param api the session api
     * @return an instance of the session repository
     */
    @Singleton
    @Provides
    fun provideSessionRepository(
        api: SessionApi,
        @ApplicationContext context: Context
    ): SessionRepository {
        return SessionImplementation(api, context)
    }


    /**
     * providing an instance of retrofit for making network requests
     * @author Ömer Aynaci
     * @return the session api
     */
    @Singleton
    @Provides
    fun provideSessionApi(@ApplicationContext context: Context): SessionApi {
        return authorizationHeader(getToken(context)).create(SessionApi::class.java)
    }
}
