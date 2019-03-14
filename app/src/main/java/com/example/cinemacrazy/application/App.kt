package com.example.cinemacrazy.application

import android.app.Application
import android.content.Context
import com.example.cinemacrazy.di.modules.ContextModule
import com.example.cinemacrazy.di.components.DaggerMoviesComponent
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class App : Application(), AnkoLogger {

    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
    }

    fun context(): Context {
        return context
    }
}