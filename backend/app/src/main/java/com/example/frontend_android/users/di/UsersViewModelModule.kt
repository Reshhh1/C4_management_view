package com.example.frontend_android.users.di

import com.example.frontend_android.users.domain.use_case.CreateUserUseCase
import com.example.frontend_android.users.presentation.RegistrationViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class UsersViewModelModule {

    /**
     * provides an instance of the registration view model
     * @author Ömer Aynaci
     * @param registrationUseCase the registration use case
     * @return an instance of the registration view model
     */
    @Provides
    @ViewModelScoped
    fun provideRegistrationViewModel(
        registrationUseCase: CreateUserUseCase
    ): RegistrationViewModel {
        return RegistrationViewModel(registrationUseCase)
    }
}