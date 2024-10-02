package com.example.frontend_android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class FrontendAndroidApplication : Application() {

    /**
     * The onCreate gets called when the application is launched
     * @author Ömer Aynaci
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}