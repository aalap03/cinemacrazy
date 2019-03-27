package com.example.cinemacrazy.medialist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.BaseMedia

class CinemaViewModel : ViewModel() {

    lateinit var factory: CinemaDataSourceFactory
    var mutableMediaType = MutableLiveData<String>()

    fun getMediaLive(api: TmdbService, mediaType: String, query: String?, cinemaListType: String): LiveData<PagedList<BaseMedia>> {


        factory = CinemaDataSourceFactory(api, mediaType, query, cinemaListType)

        clearPreviousRequest()

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