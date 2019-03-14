package com.example.cinemacrazy.apiservice

import com.example.cinemacrazy.BuildConfig
import com.example.cinemacrazy.datamodel.MovieInfo
import com.example.cinemacrazy.datamodel.TrendingMoviesResponse
import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TmdbService {

    //https://api.themoviedb.org/3/trending/all/day?api_key=70ca97bc3cbdc59a4577ea0dbeb9da00
    @GET("trending/all/{time}?api_key=${BuildConfig.TMDB_API}")
    fun getTrendingMovies(@Path("time") time: String)
            : Flowable<Response<TrendingMoviesResponse>>

    //https://api.themoviedb.org/3/movie/399361?api_key=70ca97bc3cbdc59a4577ea0dbeb9da00
    @GET("movie/{movieId}?api_key=${BuildConfig.BASE_TMDB_API}")
    fun getMovieInfo(@Path("movieId") movieId: Int)
    :Flowable<Response<MovieInfo>>
}