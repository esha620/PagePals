package com.example.pagepals1

import android.app.Application

class PagePalsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize NetworkMonitor as a singleton
        NetworkMonitor.initialize(this)
    }
}
