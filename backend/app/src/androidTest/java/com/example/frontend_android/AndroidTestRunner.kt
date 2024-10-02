package com.example.frontend_android

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

@Suppress("unused")
class AndroidTestRunner : AndroidJUnitRunner() {

    /**
     * creates a new application for the android test
     * @author Ömer Aynaci
     * @param cl The ClassLoader with which to instantiate the object.
     * @param className The name of the class implementing the Application object.
     * @param context The context to initialize the application with
     * @return The newly instantiated Application object.
     */
    override fun newApplication(
        classLoader: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(classLoader, HiltTestApplication::class.java.name, context)
    }
}