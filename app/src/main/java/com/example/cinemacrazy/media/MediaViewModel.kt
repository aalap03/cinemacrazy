package com.example.cinemacrazy.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.datamodel.BaseMedia

class MediaViewModel : ViewModel() {

    lateinit var factory: MediaDataSourceFactory
    var mutableMediaType = MutableLiveData<String>()

    fun getMediaLive(api: TmdbService, mediaType: String, query: String?): LiveData<PagedList<BaseMedia>> {


        factory = MediaDataSourceFactory(api, mediaType, query)

        val config = PagedList.Config
            .Builder()
            .setEnablePlaceholders(true)
            .setPageSize(10)
            .build()

        val livePagedListBuilder = LivePagedListBuilder(factory, config)

        return livePagedListBuilder.build()
    }

    private fun clearPreviousRequest() {
        factory.clear()
    }

    override fun onCleared() {
        super.onCleared()
        clearPreviousRequest()
    }

    fun setCurrentMediaType(type: String) {
        mutableMediaType.postValue(type)
    }

    fun getCurrentMediaType(): MutableLiveData<String> {
        return mutableMediaType
    }
}