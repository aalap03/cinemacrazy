package com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses

import android.os.Parcelable
import com.example.cinemacrazy.datamodel.utils.constant.CINEMA_TYPE_TV
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseTV(
    @SerializedName("page")
    @Expose
    var page: Int? = null,
    @SerializedName("results")
    @Expose
    var tvs: ArrayList<TrendingTv>? = arrayListOf(),
    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null,
    @SerializedName("total_results")
    @Expose
    var totalResults: Int? = null

) : Parcelable

@Parcelize
data class TrendingTv(
    @SerializedName("original_name")
    @Expose
    var originalName: String = "",
    @SerializedName("id")
    @Expose
    var id: Long = 0,
    @SerializedName("vote_count")
    @Expose
    var voteCount: Long = 0,
    @SerializedName("vote_average")
    @Expose
    var voteAvrg: Double = 0.0,
    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null,
    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String? = null,
    @SerializedName("first_air_date")
    @Expose
    var firstAirDate: String = "",
    @SerializedName("genre_ids")
    @Expose
    var genreIds: ArrayList<Long> = arrayListOf(),

    @SerializedName("overview")
    @Expose
    var overView: String = ""
) : BaseMedia, Parcelable {
    override fun mediaType(): String {
        return CINEMA_TYPE_TV
    }

    override fun getMediaId(): Long {
        return id
    }

    override fun getName(): String {
        return originalName
    }

    override fun backdropPath(): String? {
        return backdropPath
    }

    override fun posterPath(): String? {
        return posterPath
    }

    override fun genreIds(): ArrayList<Long> {
        return genreIds
    }

    override fun voteCount(): Long {
        return voteCount
    }

    override fun voteAvrg(): Double {
        return voteAvrg
    }

    override fun relaeseDate(): String {
        return firstAirDate
    }

    override fun overView(): String {
        return overView
    }
}

data class TvDetails(
    @SerializedName("episode_run_time")
    @Expose
    var listOfRuntimes: ArrayList<Int> = arrayListOf(),
    @SerializedName("genres")
    @Expose
    var genres: ArrayList<Genre> = arrayListOf(),
    @SerializedName("homepage")
    @Expose
    var homepage: String? = null,
    @SerializedName("id")
    @Expose
    var id: Int? = 0
) : BaseCinemaDetail {
    override fun homepage(): String? {
        return homepage
    }

    override fun runtime(): Int {
        return if (listOfRuntimes.isNotEmpty() && listOfRuntimes.size > 1)
            listOfRuntimes[1]
        else if (listOfRuntimes.isNotEmpty())
            listOfRuntimes[0]
        else
            0
    }

    override fun genre(): ArrayList<Genre> {
        return genres
    }
}





