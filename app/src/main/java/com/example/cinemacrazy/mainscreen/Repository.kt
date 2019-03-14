package com.example.cinemacrazy.mainscreen

import com.example.cinemacrazy.datamodel.TrendingMovie
import io.reactivex.Flowable

interface Repository {

    fun getTrendingMovies(): Flowable<ArrayList<TrendingMovie>?>
}