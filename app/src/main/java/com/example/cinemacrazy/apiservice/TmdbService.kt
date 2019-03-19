package com.example.cinemacrazy.apiservice

import com.example.cinemacrazy.BuildConfig
import com.example.cinemacrazy.datamodel.*
import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {

    //https://api.themoviedb.org/3/trending/all/day?api_key=70ca97bc3cbdc59a4577ea0dbeb9da00
    @GET("trending/movie/week?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getTrendingMovies(@Query("page") pageNum: Long)
            : Flowable<Response<ResponseMovie>>

    @GET("trending/tv/week?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getTrendingTv(@Query("page") pageNum: Long): Flowable<Response<ResponseTV>>

    //https://api.themoviedb.org/3/search/tv?api_key=70ca97bc3cbdc59a4577ea0dbeb9da00&language=en-US&page=1&query=night
    @GET("search/movie?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getSearchMovies(@Query("query") query: String, @Query("page")pageNum: Long): Flowable<Response<ResponseMovie>>

    @GET("search/tv?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getSearchTV(@Query("query") query: String, @Query("page") pageNum: Long): Flowable<Response<ResponseTV>>

    //https://api.themoviedb.org/3/movie/399361?api_key=70ca97bc3cbdc59a4577ea0dbeb9da00
    @GET("movie/{movieId}?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getMovieInfo(@Path("movieId") movieId: Long)
            : Flowable<Response<MovieDetails>>


    //https://api.themoviedb.org/3/movie/399361?api_key=70ca97bc3cbdc59a4577ea0dbeb9da00
    @GET("tv/{tvId}?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getTvInfo(@Path("tvId") tvId: Long)
            : Flowable<Response<TvDetails>>

    @GET("movie/{movieId}/videos?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getMovieVideos(@Path("movieId") movieId: Long): Flowable<Response<VideoResult>>

    @GET("movie/{movieId}/images?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getMovieImages(@Path("movieId") movieId: Long): Flowable<Response<ImageResult>>

}