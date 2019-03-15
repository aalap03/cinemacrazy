package com.example.cinemacrazy.mainscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.datamodel.TrendingMovie

class MoviesViewModel: ViewModel() {

    lateinit var factory: DatasourceFactory

    fun getLiveMovies(api: TmdbService): LiveData<PagedList<TrendingMovie>> {


        factory = DatasourceFactory(api)

        val config = PagedList.Config
            .Builder()
            .setEnablePlaceholders(true)
            .setPageSize(10)
            .build()

        val livePagedListBuilder = LivePagedListBuilder(factory, config)

        return livePagedListBuilder.build()
    }

    override fun onCleared() {
        super.onCleared()
        factory.clear()
    }
}