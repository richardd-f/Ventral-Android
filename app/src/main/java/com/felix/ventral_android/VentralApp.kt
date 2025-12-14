package com.felix.ventral_android

import android.app.Application
import com.cloudinary.android.MediaManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VentralApp : Application(){
    override fun onCreate() {
        super.onCreate()

        // Cloudinary Configuration
        val config = HashMap<String, String>()
        config["cloud_name"] = "dxfh5vzgh"
        config["secure"] = "true"
        MediaManager.init(this, config)
    }
}