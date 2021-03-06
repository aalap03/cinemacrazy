package com.example.cinemacrazy.medialist

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.BaseMedia
import org.jetbrains.anko.error

class CinemaDataSourceFactory(var api: TmdbService,
                              mediaType: String,
                              query: String?,
                              cinemaListType: String) : DataSource.Factory<Long, BaseMedia>() {

    var mediaDataSource = CinemaDataSource(api, mediaType = mediaType, query = query, cinemaListType = cinemaListType)

    override fun create(): DataSource<Long, BaseMedia> = mediaDataSource

    fun initialLoading(): MutableLiveData<Boolean> {
        return mediaDataSource.initialLoading
    }
    fun clear() {
        mediaDataSource.clear()
    }

    fun getEmptyState(): MutableLiveData<Boolean> {
        return mediaDataSource.emptyState
    }

    fun getErrorMsg(): MutableLiveData<String> {
        return mediaDataSource.errorMessage
    }

}