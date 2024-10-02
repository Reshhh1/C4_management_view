package com.example.features.user.util.di.modules

import com.example.util.converters.*
import dagger.*

@Module
class UserFactoryModule {

    /**
     * Provides an instance of the user factory
     * @author Reshwan Barhoe
     * @return an instance of UserFactory
     */
    @Provides
    fun provideUserFactory(): UserFactory {
        return UserFactory()
    }
}