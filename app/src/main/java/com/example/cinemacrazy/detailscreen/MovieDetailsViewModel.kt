package com.example.cinemacrazy.detailscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.application.AppDb
import com.example.cinemacrazy.datamodel.*
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import kotlin.collections.ArrayList

class MovieDetailsViewModel : ViewModel(), AnkoLogger {


    var compositeDisposable = CompositeDisposable()
    var mutableImageLinks = MutableLiveData<List<Image>>()
    var mutableVideoLinks = MutableLiveData<List<Video>>()

    fun getVideoLinks(movieId: Int, api: TmdbService) {

        compositeDisposable.add(
            api.getMovieVideos(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (t.isSuccessful) {
                        var list = t.body()?.videos
                        info { "Videos-> ${list?.size}" }
                        mutableVideoLinks.postValue(list)
                    } else {
                        info { "Video res Error: ${t.errorBody()?.string()}" }
                    }
                }, { t ->
                    t.printStackTrace()
                    info { "Videos Error: ${t.localizedMessage}" }
                })
        )
    }

    fun getImageLinks(movieId: Int, api: TmdbService) {
        compositeDisposable.add(
            api.getMovieImages(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    if (t.isSuccessful) {
                        var list = t.body()?.images
                        info { "Images-> ${list?.size}" }
                        mutableImageLinks.postValue(list)
                    } else {
                        info { "Images res Error: ${t.errorBody()?.string()}" }
                    }
                }, { t ->
                    t.printStackTrace()
                    info { "IMages Error: ${t.localizedMessage}" }
                })
        )
    }

    fun saveMovie(
        movieId: Int,
        api: TmdbService,
        database: AppDb
    ) {
        val movieImages = getImageResult(movieId, api)
        val movieVideos = getVideoResult(movieId, api)
        val serverMovieInfo = getServerMovieInfo(movieId, api)

        Flowable.zip(movieImages, movieVideos, serverMovieInfo, Function3<ImagesServerResult, VideosServerResult, MovieServerResult, MovieInfo> { movieImage, movieVideo, movieInfo ->
            MovieInfo()
        }).map {
            database.moviesDao().insertMovie(it)
        }.map {
            database.moviesDao().getMovie(movieId)
        }
    }

    private fun getServerMovieInfo(movieId: Int, api: TmdbService): Flowable<MovieServerResult> {
        return api.getMovieInfo(movieId)
            .map { t -> MovieServerResult(t.body() ?: Movie(), t.errorBody()?.string()) }
            .onErrorReturn { MovieServerResult(Movie(), it.localizedMessage) }

    }

    private fun getImageResult(movieId: Int, api: TmdbService): Flowable<ImagesServerResult> {

        return api.getMovieImages(movieId).map {

            ImagesServerResult(it.body()?.images ?: arrayListOf(), it.errorBody()?.string())

        }.onErrorReturn { ImagesServerResult(arrayListOf(), it.localizedMessage) }
    }

    private fun getVideoResult(movieId: Int, api: TmdbService): Flowable<VideosServerResult> {

        return api.getMovieVideos(movieId).map {

            VideosServerResult(it.body()?.videos ?: arrayListOf(), it.errorBody()?.string())

        }.onErrorReturn { VideosServerResult(arrayListOf(), it.localizedMessage) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

data class ImagesServerResult(var list: ArrayList<Image>, var errorMsg: String?)
class VideosServerResult(var list: List<Video>, var errorMsg: String?)
class MovieServerResult(var movie: Movie, var errorMsg: String?)
