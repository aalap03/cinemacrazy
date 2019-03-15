package com.example.cinemacrazy.mainscreen

import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.datamodel.TrendingMovie
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MoviesModel(var api: TmdbService): Repository, AnkoLogger {

    override fun getTrendingMovies(pageNum: Long): Flowable<ArrayList<TrendingMovie>?> {

        info { "pageNum: $pageNum" }

        return api.getTrendingMovies(pageNum)
            .map { t -> t.body()?.movies }
            .onErrorReturn { arrayListOf() }
            .subscribeOn(Schedulers.io())

    }
}