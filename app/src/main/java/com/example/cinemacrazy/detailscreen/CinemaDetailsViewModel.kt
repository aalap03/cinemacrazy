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

class CinemaDetailsViewModel : ViewModel(), AnkoLogger {

    var compositeDisposable = CompositeDisposable()

    var movieInfo = MutableLiveData<MovieInfo>()

    fun getSavedMoviesDetails(
        movieId: Long,
        api: TmdbService,
        database: AppDb
    ) {
                val movieImages = getImageResult(movieId, api)
                val movieVideos = getVideoResult(movieId, api)
                val serverMovieInfo = getServerMovieInfo(movieId, api)

                Flowable.zip(
                    movieImages,
                    movieVideos,
                    serverMovieInfo,
                    Function3<ImagesServerResult, VideosServerResult, MovieServerResult, MovieInfo> { movieImage, movieVideo, movieInfo ->
                        getMovieInfo(movieInfo, movieVideo, movieImage, movieId)
                    })
                    .map {
                        database.moviesDao().insertMovie(it)
                    }.map {
                        database.moviesDao().getMovie(movieId)
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t ->
                        movieInfo.postValue(t)
                    }, { t ->
                        t.printStackTrace()
                        info { t.localizedMessage }
                    })?.let { compositeDisposable.add(it) }
    }

    private fun getMovieInfo(
        movieInfo: MovieServerResult,
        movieVideo: VideosServerResult,
        movieImage: ImagesServerResult,
        movieId: Long
    ): MovieInfo {
        val genres: MutableList<MediaGenres> = mutableListOf()
        val videos: MutableList<VideoPath> = mutableListOf()
        val images: MutableList<ImagePath> = mutableListOf()

        var homePage = ""
        var runtime = 0

        if (movieInfo.errorMsg == null) {
            movieInfo.movie.genres.forEach {
                genres.add(MediaGenres(it.id, it.name, movieId))
            }
            homePage = movieInfo.movie.homepage!!
            runtime = movieInfo.movie.runtime!!
        }

        movieVideo.list.toMutableList().forEach {
            videos.add(VideoPath(it.key, movieId))
        }
        movieImage.list.toMutableList().forEach {
            images.add(ImagePath(it.filePath, movieId))
        }

        return MovieInfo(
            movieId.toLong(),
            runTimeMinutes = runtime,
            homePageLink = homePage,
            videos = videos,
            images = images
        )
    }

    private fun getServerMovieInfo(movieId: Long, api: TmdbService): Flowable<MovieServerResult> {
        return api.getMovieInfo(movieId)
            .map { t -> MovieServerResult(t.body() ?: Movie(), t.errorBody()?.string()) }
            .onErrorReturn { MovieServerResult(Movie(), it.localizedMessage) }

    }

    private fun getImageResult(movieId: Long, api: TmdbService): Flowable<ImagesServerResult> {

        return api.getMovieImages(movieId).map {

            ImagesServerResult(it.body()?.images ?: arrayListOf(), it.errorBody()?.string())

        }.onErrorReturn { ImagesServerResult(arrayListOf(), it.localizedMessage) }
    }

    private fun getVideoResult(movieId: Long, api: TmdbService): Flowable<VideosServerResult> {

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
