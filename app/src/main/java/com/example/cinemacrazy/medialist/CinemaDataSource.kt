package com.example.cinemacrazy.medialist

import androidx.paging.PageKeyedDataSource
import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.BaseMedia
import com.example.cinemacrazy.datamodel.utils.CINEMA_TYPE_MOVIE
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class CinemaDataSource(var api: TmdbService, var mediaType: String, private val query: String?) :
    PageKeyedDataSource<Long, BaseMedia>(),
    AnkoLogger {

    private var disposable: Disposable? = null

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

        val list = mutableListOf<BaseMedia>()

        disposable = if (mediaType == CINEMA_TYPE_MOVIE) {

            val flowableMovies = if (query == null)
                api.getTrendingMovies(pageNum)
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

            val flowableTv = if (query == null)
                api.getTrendingTv(pageNum)
            else
                api.getSearchTV(query, pageNum)

            flowableTv.map { t ->
                if (t.isSuccessful) {
                    val moviesResponse = t.body()
                    list.addAll(moviesResponse?.tvs ?: arrayListOf())
                    info { "result: ${list.size}" }
                    list
                } else {
                    throw RuntimeException(t.errorBody()?.string())
                }
            }.onErrorReturn {
                throw RuntimeException(it.localizedMessage)
            }
        }
            .subscribe({ t ->
                initialCallback?.onResult(t, null, 2)
                afterCallback?.onResult(t, pageNum + 1)
            }, { t ->
                info { "onError: ${t.localizedMessage}" }
            })

    }

    fun clear() {
        disposable?.dispose()
    }
}