package com.example.cinemacrazy.detailscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cinemacrazy.R
import com.example.cinemacrazy.apiservice.TmdbService
import javax.inject.Inject

class MovieDetailScreen: AppCompatActivity() {

    @Inject
    lateinit var retrofit: TmdbService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_detail_screen)


    }
}