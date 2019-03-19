package com.example.cinemacrazy.detailscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.application.AppDb
import com.example.cinemacrazy.datamodel.*
import com.example.cinemacrazy.datamodel.daos.ImagesDao
import com.example.cinemacrazy.datamodel.daos.VideosDao
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Response
import kotlin.concurrent.thread

class CinemaDetailsViewModel : ViewModel(), AnkoLogger {

    var compositeDisposable = CompositeDisposable()

    var imagesLoading = MutableLiveData<Boolean>()
    var videosLoading = MutableLiveData<Boolean>()
    var liveImagePaths = MutableLiveData<MutableList<MovieMedia>>()
    var liveVideoPaths = MutableLiveData<MutableList<MovieMedia>>()
    var listImageMedia = mutableListOf<MovieMedia>()
    var listVideoMedia = mutableListOf<MovieMedia>()

    var liveCinemaInfo = MutableLiveData<CinemaInfo>()

    var cinemaId: Long = 0
    lateinit var api: TmdbService
    lateinit var database: AppDb
    lateinit var cinemaType: String


    fun requestCinemaDetails(
        cinemaId: Long,
        api: TmdbService,
        database: AppDb,
        cinemaType: String
    ) {

        this.cinemaId = cinemaId
        this.api = api
        this.database = database
        this.cinemaType = cinemaType

        thread {
            val moviesDao = database.cinemaDao()
            val imagesDao = database.imagesDao()
            val videoDao = database.videosDao()

            if (cinemaType == CINEMA_TYPE_MOVIE) {
                val movieInfo = moviesDao.getCinema(cinemaId, cinemaType)

                if (movieInfo == null) {

                    imagesLoading.postValue(true)
                    videosLoading.postValue(true)

                    val cinemaInfo = CinemaInfo()
                    cinemaInfo.id = cinemaId
                    cinemaInfo.cinemaType = cinemaType

                    val movieInfo1 = api.getMovieInfo(cinemaId)
                    val movieImages = api.getMovieImages(cinemaId)
                    val movieVideos = api.getMovieVideos(cinemaId)

                    Flowable.zip(movieInfo1, movieImages, movieVideos,
                        Function3<Response<MovieDetails>, Response<ImageResult>, Response<VideoResult>, CinemaInfo> { t1, t2, t3 ->
                            saveCinemaInfo(t1, cinemaInfo)
                            saveImages(t2, imagesDao, cinemaInfo)
                            saveVideos(t3, videoDao, cinemaInfo)
                            cinemaInfo
                        })
                        .subscribe({ t ->
                            moviesDao.insertCinema(t)
                        }, { t ->
                            moviesDao.insertCinema(cinemaInfo)
                        }, {
                            imagesLoading.postValue(false)
                            videosLoading.postValue(false)
                        })

                } else {
                    val images = movieInfo.images
                    val videos = movieInfo.videos

                    if (images == null) {
                        imagesLoading.postValue(true)
                    } else {
                        listImageMedia.addAll(images)
                        liveImagePaths.postValue(listImageMedia)
                        imagesLoading.postValue(false)
                    }

                    if (videos == null) {
                        videosLoading.postValue(true)
                    } else {
                        listVideoMedia.addAll(videos)
                        liveVideoPaths.postValue(listVideoMedia)
                        videosLoading.postValue(false)
                    }
                }
            } else {

            }
        }

    }

    private fun saveVideos(
        t3: Response<VideoResult>,
        videoDao: VideosDao,
        cinemaInfo: CinemaInfo
    ) {
        if (t3.isSuccessful) {
            val videoResult = t3.body()
            videosLoading.postValue(false)
            val videoPath = mutableListOf<VideoPath>()
            videoResult?.videos?.forEach {
                videoPath.add(VideoPath(it.key, cinemaId, cinemaType))
            }
            videoDao.insertVideos(videoPath)
            cinemaInfo.videos = videoPath
            listVideoMedia.addAll(videoPath)
            liveVideoPaths.postValue(listVideoMedia)
        } else {

        }
    }

    private fun saveCinemaInfo(
        t1: Response<MovieDetails>,
        cinemaInfo: CinemaInfo
    ) {
        if (t1.isSuccessful) {
            val body = t1.body()
            cinemaInfo.homePageLink = body?.homepage
            cinemaInfo.runTimeMinutes = body?.runtime ?: 0
        } else {

        }
    }

    private fun saveImages(
        t2: Response<ImageResult>,
        imagesDao: ImagesDao,
        cinemaInfo: CinemaInfo
    ) {
        if (t2.isSuccessful) {
            val imageResult = t2.body()
            imagesLoading.postValue(false)
            val imagePaths = mutableListOf<ImagePath>()
            imageResult?.images?.forEach {
                imagePaths.add(ImagePath(it.filePath, cinemaId, cinemaType))
            }
            imagesDao.insertImages(imagePaths)
            cinemaInfo.images = imagePaths
            listImageMedia.addAll(imagePaths)
            liveImagePaths.postValue(listImageMedia)
        } else {

        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}