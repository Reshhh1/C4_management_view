package com.example.frontend_android.users.di

import android.content.Context
import com.example.frontend_android.auth.data.local.getToken
import com.example.frontend_android.auth.data.remote.network.authorizationHeader
import com.example.frontend_android.users.data.remote.UserApi
import com.example.frontend_android.users.data.repository.UserImplementation
import com.example.frontend_android.users.data.repository.UserRepository
import com.example.frontend_android.users.domain.use_cases.SearchUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserModule {

    /**
     * Provides the UserRepository
     * @param userAPI the required dependency
     * @author Reshwan Barhoe
     * @return UserRepository
     */
    @Provides
    @Singleton
    fun provideUserRepository(
        userAPI: UserApi
    ): UserRepository {
        return UserImplementation(userAPI)
    }

    /**
     * Provides the SearchuserUseCase
     * @param  userRepository the required dependency
     * @author Reshwan Barhoe
     * @return SearchUserUseCase
     */
    @Provides
    @Singleton
    fun provideSearchUserUseCase(
        userRepository: UserRepository
    ): SearchUserUseCase {
        return SearchUserUseCase(userRepository)
    }

    /**
     * Provides the userApi
     * @param context the required dependency
     * @author Reshwan Barhoe
     * @return UserApi
     */
    @Provides
    @Singleton
    fun provideUserApi(@ApplicationContext context: Context): UserApi {
        return authorizationHeader(getToken(context)).create(UserApi::class.java)
    }
}