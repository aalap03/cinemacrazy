package com.example.cinemacrazy.mainscreen

import androidx.paging.DataSource
import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.datamodel.TrendingMovie

class DatasourceFactory(var api: TmdbService) : DataSource.Factory<Long, TrendingMovie>() {

    var dataSource = MoviesDataSource(api)

    override fun create(): DataSource<Long, TrendingMovie> = dataSource

    fun clear() {
        dataSource.clear()
    }

}