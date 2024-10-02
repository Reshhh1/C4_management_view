package com.example.features.user.util.di.modules

import com.example.features.user.data.repository.*
import com.example.util.converters.*
import dagger.*

@Module
class UserRepositoryModule {

    /**
     * Provides an instance of the user repository
     * @author Reshwan Barhoe
     * @param userFactory needed dependency
     * @return an instance of UserRepository
     */
    @Provides
    fun provideUserRepository(userFactory: UserFactory): UserRepository {
        return UserRepository(userFactory)
    }
}