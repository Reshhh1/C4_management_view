package com.example.general.di.modules

import com.example.features.team.business.service.SearchTermParser
import dagger.Module
import dagger.Provides

@Module
class SearchTermModule {

    /**
     * Provides an instance of the SearchTermParser
     * @author Reshwan Barhoe
     * @return an instance of the SearchTermParser
     */
    @Provides
    fun provideSearchTermParser(): SearchTermParser {
        return SearchTermParser()
    }
}