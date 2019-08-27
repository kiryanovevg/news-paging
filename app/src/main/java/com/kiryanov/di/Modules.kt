package com.kiryanov.di.module

import android.content.Context
import com.kiryanov.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {

    @Provides
    @Singleton
    fun provideAppContext(): Context = app
}