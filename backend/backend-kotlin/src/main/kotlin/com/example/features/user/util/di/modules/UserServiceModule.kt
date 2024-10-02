package com.example.features.user.util.di.modules

import com.example.features.team.business.service.SearchTermParser
import com.example.features.user.business.service.UserService
import com.example.features.user.data.repository.UserRepository
import com.example.util.converters.*
import dagger.Module
import dagger.Provides

@Module
class UserServiceModule {

    /**
     * Provides an instance of the user service
     * @author Reshwan Barhoe
     * @param userRepository needed dependency
     * @param searchTermParser needed dependency
     * @return an instance of the UserService
     */
    @Provides
    fun provideUserService(
        userRepository: UserRepository,
        searchTermParser: SearchTermParser,
        userFactory: UserFactory
    ): UserService {
        return UserService(
            searchTermParser = searchTermParser,
            userRepository = userRepository,
            userFactory = userFactory
        )
    }
}