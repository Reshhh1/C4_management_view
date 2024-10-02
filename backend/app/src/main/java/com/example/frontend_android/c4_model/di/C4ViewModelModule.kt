package com.example.frontend_android.c4_model.di

import com.example.frontend_android.c4_model.domain.use_cases.GetC4LayersUseCase
import com.example.frontend_android.c4_model.presentation.C4OverviewViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class C4ViewModelModule {

    /**
     * Provides the C4OverviewViewModel
     * @author Reshwan Barhoe
     * @param getC4LayersUseCase needed dependency
     * @return C4OverviewViewModel
     */
    @Provides
    @ViewModelScoped
    fun provideC4OverviewModelModule(
        getC4LayersUseCase: GetC4LayersUseCase
    ): C4OverviewViewModel {
        return C4OverviewViewModel(getC4LayersUseCase)
    }
}