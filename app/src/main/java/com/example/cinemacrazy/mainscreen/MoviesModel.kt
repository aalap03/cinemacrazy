package com.example.cinemacrazy.mainscreen

import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.datamodel.TrendingMovie
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class MoviesModel(var api: TmdbService): Repository {

    override fun getTrendingMovies(): Flowable<ArrayList<TrendingMovie>?> {

        return api.getTrendingMovies("day")
            .map { t -> t.body()?.movies }
            .onErrorReturn { arrayListOf() }
            .subscribeOn(Schedulers.io())

    }
}