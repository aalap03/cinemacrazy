package com.example.cinemacrazy.mainscreen

import androidx.paging.PageKeyedDataSource
import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.datamodel.TrendingMovie
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MoviesDataSource(var api: TmdbService) : PageKeyedDataSource<Long, TrendingMovie>(), AnkoLogger {

    var compositeDisposabel = CompositeDisposable()

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, TrendingMovie>) {
        compositeDisposabel.add(
            get(1)
                .subscribe({ t ->
                    callback.onResult(t, null, 2)
                }, { t ->
                    info { "Error: ${t.localizedMessage}" }
                })
        )
    }

    fun get(pageNum: Long): Flowable<ArrayList<TrendingMovie>> {
        var list: ArrayList<TrendingMovie>

        info { "pageNum: $pageNum" }

        return api.getTrendingMovies(pageNum)
            .map { t ->
                if (t.isSuccessful) {
                    val moviesResponse = t.body()
                    list = moviesResponse?.movies ?: arrayListOf()
                    info { "result: ${list.size}" }
                    list
                } else {
                    throw RuntimeException(t.errorBody()?.string())
                }
            }.onErrorReturn {
                throw RuntimeException(it.localizedMessage)
            }
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, TrendingMovie>) {
        compositeDisposabel.add(
            get(params.key)
                .subscribe({ t ->
                    callback.onResult(t, params.key + 1)
                }, { t ->
                    info { "Error: ${t.localizedMessage}" }
                })
        )
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, TrendingMovie>) {
//ignored
    }

    fun clear() {
        compositeDisposabel.clear()
    }


}

sealed class MoviesResult

class TrendingList(var list: ArrayList<TrendingMovie>) : MoviesResult()

object InitialLoading : MoviesResult()

object LoadMore : MoviesResult()
