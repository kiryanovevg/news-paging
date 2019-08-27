package com.kiryanov

import android.app.Application
import com.kiryanov.di.*

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = initDagger(this)
    }

    private fun initDagger(app: App): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .networkModule(NetworkModule(getString(R.string.base_url), getString(R.string.api_key)))
            .repositoryModule(RepositoryModule())
            .build()
}