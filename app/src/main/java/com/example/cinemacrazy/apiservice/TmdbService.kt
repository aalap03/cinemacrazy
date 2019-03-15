package com.example.cinemacrazy.apiservice

import com.example.cinemacrazy.BuildConfig
import com.example.cinemacrazy.datamodel.ImageResult
import com.example.cinemacrazy.datamodel.Movie
import com.example.cinemacrazy.datamodel.TrendingMoviesResponse
import com.example.cinemacrazy.datamodel.VideoResult
import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {

    //https://api.themoviedb.org/3/trending/all/day?api_key=70ca97bc3cbdc59a4577ea0dbeb9da00
    @GET("trending/all/week?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getTrendingMovies(@Query("page") pageNum: Long)
            : Flowable<Response<TrendingMoviesResponse>>

    //https://api.themoviedb.org/3/movie/399361?api_key=70ca97bc3cbdc59a4577ea0dbeb9da00
    @GET("movie/{movieId}?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getMovieInfo(@Path("movieId") movieId: Int)
            : Flowable<Response<Movie>>

    @GET("movie/{movieId}/videos?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getMovieVideos(@Path("movieId") movieId: Int): Flowable<Response<VideoResult>>

    @GET("movie/{movieId}/images?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getMovieImages(@Path("movieId") movieId: Int): Flowable<Response<ImageResult>>
}