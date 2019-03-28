package com.example.cinemacrazy.apiservice

import com.example.cinemacrazy.BuildConfig
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.MovieDetails
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.ResponseMovie
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.ResponseTV
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.TvDetails
import com.example.cinemacrazy.datamodel.serverResponses.mediaResponses.ImageResult
import com.example.cinemacrazy.datamodel.serverResponses.mediaResponses.VideoResult
import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {

    @GET("trending/movie/week?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getTrendingMovies(@Query("page") pageNum: Long)
            : Flowable<Response<ResponseMovie>>

    @GET("movie/{type}?api_key=${BuildConfig.TMDB_API_KEY}&language=en-US")
    fun getTypedMovies(@Path("type") type: String, @Query("page") pageNum: Long)
            : Flowable<Response<ResponseMovie>>

    @GET("tv/{type}?api_key=${BuildConfig.TMDB_API_KEY}&language=en-US")
    fun getTypedTv(@Path("type") type: String, @Query("page") pageNum: Long)
            : Flowable<Response<ResponseTV>>

    @GET("trending/tv/week?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getTrendingTv(@Query("page") pageNum: Long): Flowable<Response<ResponseTV>>

    @GET("search/movie?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getSearchMovies(@Query("query") query: String, @Query("page") pageNum: Long): Flowable<Response<ResponseMovie>>

    @GET("search/tv?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getSearchTV(@Query("query") query: String, @Query("page") pageNum: Long): Flowable<Response<ResponseTV>>

    @GET("movie/{movieId}?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getMovieInfo(@Path("movieId") movieId: Long)
            : Flowable<Response<MovieDetails>>

    @GET("tv/{tvId}?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getTvInfo(@Path("tvId") tvId: Long)
            : Flowable<Response<TvDetails>>

    @GET("movie/{movieId}/videos?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getMovieVideos(@Path("movieId") movieId: Long): Flowable<Response<VideoResult>>

    @GET("movie/{movieId}/images?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getMovieImages(@Path("movieId") movieId: Long): Flowable<Response<ImageResult>>

    @GET("tv/{tvId}/videos?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getTvVideos(@Path("tvId") cinemaId: Long): Flowable<Response<VideoResult>>

    @GET("tv/{tvId}/images?api_key=${BuildConfig.TMDB_API_KEY}")
    fun getTvImages(@Path("tvId") cinemaId: Long): Flowable<Response<ImageResult>>

}