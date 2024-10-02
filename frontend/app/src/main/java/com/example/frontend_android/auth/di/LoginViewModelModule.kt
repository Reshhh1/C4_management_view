package com.example.frontend_android.auth.di

import com.example.frontend_android.auth.data.repository.SessionRepository
import com.example.frontend_android.auth.presentation.login.LoginViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
class LoginViewModelModule {

    /**
     * letting hilt know how to create the login view model
     * @author Ömer Aynaci
     * @param sessionRepository the session repository
     * @return the session repository
     */
    @Provides
    @ViewModelScoped
    fun providedLoginViewModel(
        sessionRepository: SessionRepository
    ): LoginViewModel {
        return LoginViewModel(sessionRepository)
    }

}