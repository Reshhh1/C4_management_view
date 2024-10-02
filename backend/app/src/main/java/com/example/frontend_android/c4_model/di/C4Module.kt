package com.example.frontend_android.c4_model.di

import com.example.frontend_android.auth.data.remote.network.retrofitInstance
import com.example.frontend_android.c4_model.data.remote.C4Api
import com.example.frontend_android.c4_model.data.repository.C4Implementation
import com.example.frontend_android.c4_model.data.repository.C4Repository
import com.example.frontend_android.c4_model.domain.use_cases.GetC4LayersUseCase
import com.example.frontend_android.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class C4Module {

    /**
     * Provides the C4Repository
     * @param  c4Api required dependency
     * @author Reshwan Barhoe
     * @return Instance of the C4Repository
     */
    @Provides
    @Singleton
    fun provideC4Repository(
        c4Api: C4Api
    ): C4Repository {
        return C4Implementation(
            c4Api = c4Api
        )
    }

    /**
     * Provides the GetC4LayersUseCase
     * @param c4Repository the required dependency
     * @author Reshwan Barhoe
     * @return a instance of the usecase
     */
    @Provides
    @Singleton
    fun provideGetC4LayersUseCase(
        c4Repository: C4Repository
    ): GetC4LayersUseCase {
        return GetC4LayersUseCase(
            c4Repository = c4Repository
        )
    }


    /**
     * Provides the c4Api
     * @author Reshwan Barhoe
     * @return C4API
     */
    @Provides
    @Singleton
    fun provideC4Api(): C4Api {
        return retrofitInstance(
            okHttpClient = OkHttpClient(),
            requestUrl = Constants.C4_URL
        ).create(C4Api::class.java)
    }
}