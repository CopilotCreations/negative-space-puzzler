package com.negativespace.puzzler

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NegativeSpacePuzzlerApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Initialize any app-wide dependencies here
    }
}
