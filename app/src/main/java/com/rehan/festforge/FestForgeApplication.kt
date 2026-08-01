package com.rehan.festforge

import android.app.Application
import com.rehan.festforge.data.datastore.UserPreferences

class FestForgeApplication : Application() {

    lateinit var userPreferences: UserPreferences
        private set

    override fun onCreate() {
        super.onCreate()
        userPreferences = UserPreferences(this)
        // Firebase auto-initializes via google-services plugin / BOM
    }
}
