package com.example.cinemacrazy.di.components

import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.di.modules.NetworkModule
import com.example.cinemacrazy.mainscreen.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface MoviesComponent {

    fun tmdbService(): TmdbService

    fun inject(mainActivity: MainActivity)
}