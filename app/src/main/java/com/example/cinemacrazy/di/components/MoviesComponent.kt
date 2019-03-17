package com.example.cinemacrazy.di.components

import com.example.cinemacrazy.BaseActivity
import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.application.AppDb
import com.example.cinemacrazy.di.modules.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface MoviesComponent {

    fun tmdbService(): TmdbService

    fun database(): AppDb

    fun inject(mainActivity: BaseActivity)
}