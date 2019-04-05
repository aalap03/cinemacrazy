package com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses

import android.os.Parcelable
import com.example.cinemacrazy.datamodel.utils.constant.CINEMA_TYPE_MOVIE
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseMovie(
    @SerializedName("page")
    @Expose
    var page: Int? = null,
    @SerializedName("results")
    @Expose
    var movies: ArrayList<TrendingMovie>? = arrayListOf(),
    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null,
    @SerializedName("total_results")
    @Expose
    var totalResults: Int? = null

) : Parcelable

@Parcelize
data class TrendingMovie(

    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String? = null,
    @SerializedName("id")
    @Expose
    var id: Long = 0,
    @SerializedName("original_title")
    @Expose
    var originalTitle: String = "",
    @SerializedName("overview")
    @Expose
    var overview: String = "",
    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null,
    @SerializedName("release_date")
    @Expose
    var releaseDate: String = "",
    @SerializedName("title")
    @Expose
    var title: String = "",
    @SerializedName("vote_average")
    @Expose
    var voteAverage: Double = 0.0,
    @SerializedName("vote_count")
    @Expose
    var voteCount: Long = 0,
    @SerializedName("genre_ids")
    @Expose
    var genreIds: ArrayList<Long> = arrayListOf()

) : BaseMedia, Parcelable {
    override fun mediaType(): String {
        return CINEMA_TYPE_MOVIE
    }

    override fun getMediaId(): Long {
        return id
    }

    override fun getName(): String {
        return title
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
        return voteAverage
    }

    override fun releaseDate(): String {
        return releaseDate
    }

    override fun overView(): String {
        return overview
    }
}

data class MovieDetails(
    @SerializedName("genres")
    @Expose
    var genres: ArrayList<Genre> = arrayListOf(),
    @SerializedName("homepage")
    @Expose
    var homepage: String? = null,
    @SerializedName("id")
    @Expose
    var id: Int? = 0,
    @SerializedName("runtime")
    @Expose
    var runtime: Int? = null
): BaseCinemaDetail {
    override fun genre(): ArrayList<Genre> {
        return genres
    }

    override fun homepage(): String? {
        return homepage
    }

    override fun runtime(): Int {
        return runtime ?: 0
    }
}

