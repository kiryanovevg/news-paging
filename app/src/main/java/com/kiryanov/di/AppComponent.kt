package com.kiryanov.di

import com.kiryanov.ui.MainPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class, NetworkModule::class, RepositoryModule::class
])
interface AppComponent {

    fun inject(target: MainPresenter)
}