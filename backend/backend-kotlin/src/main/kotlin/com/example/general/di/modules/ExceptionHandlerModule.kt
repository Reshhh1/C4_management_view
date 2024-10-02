package com.example.general.di.modules

import com.example.general.exception.*
import dagger.*

@Module
class ExceptionHandlerModule {

    /**
     * Provides an instance of the exception handler
     * @author Reshwan Barhoe
     * @return an instance of ExceptionHandler
     */
    @Provides
    fun provideExceptionHandler(): ExceptionHandler {
        return ExceptionHandler()
    }
}