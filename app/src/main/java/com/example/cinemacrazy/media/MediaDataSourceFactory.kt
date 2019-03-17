package com.example.cinemacrazy.media

import androidx.paging.DataSource
import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.datamodel.BaseMedia

class MediaDataSourceFactory(var api: TmdbService,
                             mediaType: String,
                             query: String?) : DataSource.Factory<Long, BaseMedia>() {

    var mediaDataSource = MediaDataSource(api, mediaType = mediaType, query = query)

    override fun create(): DataSource<Long, BaseMedia> = mediaDataSource

    fun clear() {
        mediaDataSource.clear()
    }

}