package com.example.cinemacrazy.medialist

import androidx.paging.DataSource
import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.BaseMedia

class CinemaDataSourceFactory(var api: TmdbService,
                              mediaType: String,
                              query: String?) : DataSource.Factory<Long, BaseMedia>() {

    var mediaDataSource = CinemaDataSource(api, mediaType = mediaType, query = query)

    override fun create(): DataSource<Long, BaseMedia> = mediaDataSource

    fun clear() {
        mediaDataSource.clear()
    }

}