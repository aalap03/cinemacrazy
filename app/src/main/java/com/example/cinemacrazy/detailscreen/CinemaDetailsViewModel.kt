package com.example.cinemacrazy.detailscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.application.AppDb
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.BaseCinemaDetail
import com.example.cinemacrazy.datamodel.serverResponses.mediaResponses.ImageResult
import com.example.cinemacrazy.datamodel.serverResponses.mediaResponses.MovieMedia
import com.example.cinemacrazy.datamodel.serverResponses.mediaResponses.VideoResult
import com.example.cinemacrazy.datamodel.room.CinemaInfo
import com.example.cinemacrazy.datamodel.room.ImagePath
import com.example.cinemacrazy.datamodel.room.VideoPath
import com.example.cinemacrazy.datamodel.room.daos.CinemaDao
import com.example.cinemacrazy.datamodel.room.daos.ImagesDao
import com.example.cinemacrazy.datamodel.room.daos.VideosDao
import com.example.cinemacrazy.datamodel.utils.CINEMA_TYPE_MOVIE
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
    var cinemaImages = MutableLiveData<MutableList<MovieMedia>>()
    var cinemaVideos = MutableLiveData<MutableList<MovieMedia>>()

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
            val cinemaDao = database.cinemaDao()
            val imagesDao = database.imagesDao()
            val videoDao = database.videosDao()

            if (cinemaType == CINEMA_TYPE_MOVIE) {
                val dbMovieInfo = cinemaDao.getCinema(cinemaId, cinemaType)

                if (dbMovieInfo == null) {

                    imagesLoading.postValue(true)
                    videosLoading.postValue(true)

                    val cinemaInfo = CinemaInfo()
                    cinemaInfo.id = cinemaId
                    cinemaInfo.cinemaType = cinemaType
                    startSaving(cinemaInfo, imagesDao, videoDao, cinemaDao, api.getMovieInfo(cinemaId).map { it })

                } else {
                    liveCinemaInfo.postValue(dbMovieInfo)
                    lookForImagesAndVideos(dbMovieInfo, imagesDao, videoDao)
                }
            } else {
                val dbTvInfo = cinemaDao.getCinema(cinemaId, cinemaType)
                if (dbTvInfo == null) {
                    imagesLoading.postValue(true)
                    videosLoading.postValue(true)

                    val cinemaInfo = CinemaInfo()
                    cinemaInfo.id = cinemaId
                    cinemaInfo.cinemaType = cinemaType

                    val tvInfo = api.getTvInfo(cinemaId)
                    startSaving(cinemaInfo, imagesDao, videoDao, cinemaDao, tvInfo.map { it })

                } else {
                    liveCinemaInfo.postValue(dbTvInfo)
                    lookForImagesAndVideos(dbTvInfo, imagesDao, videoDao)
                }
            }
        }

    }

    private fun lookForImagesAndVideos(
        dbMovieInfo: CinemaInfo,
        imagesDao: ImagesDao,
        videoDao: VideosDao
    ) {
        val images = dbMovieInfo.images
        val videos = dbMovieInfo.videos

        if (images == null) {
            val imagesForCinema = imagesDao.getImagesForCinema(cinemaId, cinemaType)
            if (imagesForCinema?.value?.isNotEmpty() == true) {
                listImageMedia.addAll(imagesForCinema?.value ?: mutableListOf())
                cinemaImages.postValue(listImageMedia)
            } else {
                imagesLoading.postValue(true)

                compositeDisposable.add(
                    (if (cinemaType == CINEMA_TYPE_MOVIE) api.getMovieImages(cinemaId) else api.getTvImages(cinemaId))
                        .subscribe({ t ->
                            saveImages(t, imagesDao, dbMovieInfo)
                        }, { t ->
                            info { "error: while getting images ${t.localizedMessage}" }
                        })
                )
            }
        } else {
            listImageMedia.addAll(images)
            cinemaImages.postValue(listImageMedia)
            imagesLoading.postValue(false)
        }

        if (videos == null) {
            val videosForCinema = videoDao.getVideosForCinema(cinemaId, cinemaType)
            if (videosForCinema?.value?.isNotEmpty() == true) {
                listVideoMedia.addAll(videosForCinema?.value ?: mutableListOf())
                cinemaVideos.postValue(listVideoMedia)
            } else {
                videosLoading.postValue(true)
                compositeDisposable.add(
                    (if (cinemaType == CINEMA_TYPE_MOVIE) api.getMovieVideos(cinemaId) else api.getTvVideos(cinemaId))
                        .subscribe({ t ->
                            saveVideos(t, videoDao, dbMovieInfo)
                        }, { t ->
                            info { "error: while getting videos ${t.localizedMessage}" }
                        })
                )
            }
        } else {
            listVideoMedia.addAll(videos)
            cinemaVideos.postValue(listVideoMedia)
            videosLoading.postValue(false)
        }
    }

    private fun startSaving(
        cinemaInfo: CinemaInfo,
        imagesDao: ImagesDao,
        videoDao: VideosDao,
        cinemaDao: CinemaDao,
        baseCinemaDetailResponse: Flowable<Response<out BaseCinemaDetail>>
    ) {

        val imageResultRes =
            if (cinemaType == CINEMA_TYPE_MOVIE) api.getMovieImages(cinemaId) else api.getTvImages(cinemaId)
        val videoResultRes =
            if (cinemaType == CINEMA_TYPE_MOVIE) api.getMovieVideos(cinemaId) else api.getTvVideos(cinemaId)

        compositeDisposable.add(
            Flowable.zip(baseCinemaDetailResponse, imageResultRes, videoResultRes,
                Function3<Response<out BaseCinemaDetail>, Response<ImageResult>, Response<VideoResult>, CinemaInfo>
                { t1, t2, t3 ->
                    saveCinemaInfo(t1, cinemaInfo)
                    saveImages(t2, imagesDao, cinemaInfo)
                    saveVideos(t3, videoDao, cinemaInfo)
                    cinemaInfo
                })
                .subscribe({ t ->
                    cinemaDao.insertCinema(t)
                    liveCinemaInfo.postValue(t)
                }, { t ->
                    t.printStackTrace()
                    info { "error: ${t.localizedMessage}" }
                    cinemaDao.insertCinema(cinemaInfo)
                }, {
                    imagesLoading.postValue(false)
                    videosLoading.postValue(false)
                })
        )
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
            cinemaVideos.postValue(listVideoMedia)
        } else {

        }
    }

    private fun saveCinemaInfo(
        t1: Response<out BaseCinemaDetail>,
        cinemaInfo: CinemaInfo
    ) {
        if (t1.isSuccessful) {
            val body = t1.body()
            cinemaInfo.homePageLink = body?.homepage()
            cinemaInfo.runTimeMinutes = body?.runtime() ?: 0
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
            cinemaImages.postValue(listImageMedia)
        } else {

        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}