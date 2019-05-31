package com.example.cinemacrazy

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.application.App
import com.example.cinemacrazy.application.AppDb
import com.example.cinemacrazy.di.components.DaggerMoviesComponent
import com.example.cinemacrazy.di.modules.ContextModule
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var api: TmdbService

    @Inject
    lateinit var app: App

    @Inject
    lateinit var database: AppDb

    abstract fun getLayoutRes(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())

        val moviesComponent = DaggerMoviesComponent
            .builder()
            .contextModule(ContextModule(this))
            .build()
        moviesComponent.inject(this)
    }

}