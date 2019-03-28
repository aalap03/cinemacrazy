package com.example.cinemacrazy.medialist

import androidx.paging.PageKeyedDataSource
import com.example.cinemacrazy.apiservice.*
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.BaseMedia
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.ResponseMovie
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.ResponseTV
import com.example.cinemacrazy.datamodel.utils.constant.ApiConstants.Companion.API_CINEMALIST_TRENDING
import com.example.cinemacrazy.datamodel.utils.constant.CINEMA_TYPE_MOVIE
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Response

class CinemaDataSource(
    var api: TmdbService,
    var mediaType: String,
    private val query: String?,
    var cinemaListType: String
) :
    PageKeyedDataSource<Long, BaseMedia>(),
    AnkoLogger {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, BaseMedia>) {
        getBaseMediaResponse(api, 1, callback, null)
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, BaseMedia>) {
        getBaseMediaResponse(api, params.key, initialCallback = null, afterCallback = callback)
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, BaseMedia>) {
        //load before
    }

    private fun getBaseMediaResponse(
        api: TmdbService,
        pageNum: Long,
        initialCallback: LoadInitialCallback<Long, BaseMedia>?,
        afterCallback: LoadCallback<Long, BaseMedia>?
    ) {

        info { "Type: $cinemaListType" }

        val list = mutableListOf<BaseMedia>()

        compositeDisposable.add(if (mediaType == CINEMA_TYPE_MOVIE) {

            info { "Item: Movie-> $cinemaListType" }
            val flowableMovies = if (query == null)
                getRespectedMoviesFlowable(pageNum, cinemaListType)
            else
                api.getSearchMovies(query, pageNum)

            flowableMovies.map { t ->

                if (t.isSuccessful) {
                    val moviesResponse = t.body()
                    list.addAll(moviesResponse?.movies ?: arrayListOf())
                    info { "result: ${list.size}" }
                    list
                } else {
                    throw RuntimeException(t.errorBody()?.string())
                }
            }.onErrorReturn {
                throw RuntimeException(it.localizedMessage)
            }
        } else {

            info { "Item: TV-> $cinemaListType" }
            val flowableTv = if (query == null)
                getRespectedTvFlowable(pageNum, cinemaListType)
            else
                api.getSearchTV(query, pageNum)

            flowableTv.map { t ->
                if (t.isSuccessful) {
                    val moviesResponse = t.body()
                    list.addAll(moviesResponse?.tvs ?: arrayListOf())
                    list
                } else {
                    throw RuntimeException(t.errorBody()?.string())
                }
            }.onErrorReturn {
                throw RuntimeException(it.localizedMessage)
            }
        }
            .subscribe({ t ->
                initialCallback?.onResult(t.sortedBy { baseMedia -> baseMedia.relaeseDate() }, null, 2)
                afterCallback?.onResult(t.sortedBy { baseMedia -> baseMedia.relaeseDate() }, pageNum + 1)
            }, { t ->
                t.printStackTrace()
                info { "onError: ${t.localizedMessage}" }
            })
        )

    }

    private fun getRespectedMoviesFlowable(pageNum: Long, cinemaListType: String): Flowable<Response<ResponseMovie>> {
        info { "cinemaListType: Movie->$cinemaListType" }

        return if (cinemaListType.equals(API_CINEMALIST_TRENDING, true))
            api.getTrendingMovies(pageNum)
        else
            api.getTypedMovies(cinemaListType, pageNum)
    }


    private fun getRespectedTvFlowable(pageNum: Long, cinemaListType: String): Flowable<Response<ResponseTV>> {
        info { "cinemaListType: TV->$cinemaListType" }
        return if (cinemaListType.equals(API_CINEMALIST_TRENDING, true))
            api.getTrendingTv(pageNum)
        else
            api.getTypedTv(cinemaListType, pageNum)

    }


    fun clear() {
        compositeDisposable.clear()
    }
}