package com.example.cinemacrazy.detailscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.application.AppDb
import com.example.cinemacrazy.datamodel.*
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger
import kotlin.concurrent.thread

class CinemaDetailsViewModel : ViewModel(), AnkoLogger {

    var compositeDisposable = CompositeDisposable()

    var imagesLoading = MutableLiveData<Boolean>()
    var videosLoading = MutableLiveData<Boolean>()
    var imagePaths = MutableLiveData<MutableList<ImagePath>>()
    var videoPaths = MutableLiveData<MutableList<VideoPath>>()

    var cinemaInfo = MutableLiveData<CinemaInfo>()

    fun requestCinemaDetails(
        cinemaId: Long,
        api: TmdbService,
        database: AppDb,
        cinemaType: String
    ) {

        thread {
            val moviesDao = database.moviesDao()
            val imagesDao = database.imagesDao()
            val videoDao = database.videosDao()

            if(cinemaType == CINEMA_TYPE_MOVIE) {
                val movie = moviesDao.getMovie(cinemaId)

                if(movie == null) {

                }else{
                    val images = movie.images
                    val videos = movie.videos

                    if(images == null){

                    }

                    if(videos == null){

                    }
                }
            }else{

            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}