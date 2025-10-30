package com.smokereg

import android.app.Application
import com.smokereg.data.JsonStorageManager
import com.smokereg.data.SmokeRepository
import com.smokereg.viewmodel.DashboardViewModelFactory
import com.smokereg.viewmodel.SmokeViewModelFactory

/**
 * Application class for SmokeReg
 * Manages application-level dependencies
 */
class SmokeRegApplication : Application() {

    // Lazy initialization of dependencies
    private val jsonStorageManager: JsonStorageManager by lazy {
        JsonStorageManager(this)
    }

    val repository: SmokeRepository by lazy {
        SmokeRepository(jsonStorageManager)
    }

    val smokeViewModelFactory: SmokeViewModelFactory by lazy {
        SmokeViewModelFactory(repository)
    }

    val dashboardViewModelFactory: DashboardViewModelFactory by lazy {
        DashboardViewModelFactory(repository)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        @Volatile
        private var instance: SmokeRegApplication? = null

        fun getInstance(): SmokeRegApplication {
            return instance ?: throw IllegalStateException(
                "Application not initialized. Make sure SmokeRegApplication is declared in AndroidManifest.xml"
            )
        }
    }
}