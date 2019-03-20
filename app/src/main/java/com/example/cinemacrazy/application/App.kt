package com.example.cinemacrazy.application

import android.app.Application
import android.content.Context
import com.example.cinemacrazy.datamodel.utils.MovieGenres
import com.example.cinemacrazy.datamodel.utils.TvGenres
import com.example.cinemacrazy.datamodel.utils.moviesGenre
import com.example.cinemacrazy.datamodel.utils.tvGenreString
import com.google.gson.Gson
import org.jetbrains.anko.AnkoLogger

class App : Application(), AnkoLogger {

    lateinit var context: Context
    lateinit var tmdbGenresMovies: MovieGenres
    lateinit var tmdbGenresTV: TvGenres

    override fun onCreate() {
        super.onCreate()
    }

    fun getMoviesGenre(genreIds: ArrayList<Long>): ArrayList<String> {
        val gson = Gson()

        val list = arrayListOf<String>()
        tmdbGenresMovies = gson.fromJson(moviesGenre, MovieGenres::class.java)

        genreIds.forEach { genreId ->
            tmdbGenresMovies.genres.forEach {
                if ((it.id == genreId)) {
                    list.add(it.name)
                }
            }
        }

        return list
    }

    fun getTvGenres(genreIds: ArrayList<Long>): ArrayList<String> {
        val gson = Gson()

        tmdbGenresTV = gson.fromJson<TvGenres>(tvGenreString, TvGenres::class.java)

        val list = arrayListOf<String>()

        genreIds.forEach { genreId ->
            tmdbGenresTV.genres.forEach {
                if ((it.id == genreId)) {
                    list.add(it.name)
                }
            }
        }

        return list
    }

    fun context(): Context {
        return context
    }
}