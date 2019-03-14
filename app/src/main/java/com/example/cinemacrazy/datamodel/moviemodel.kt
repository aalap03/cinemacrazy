package com.example.cinemacrazy.datamodel

import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrendingMoviesResponse(
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
    var id: Int? = null,
    @SerializedName("original_language")
    @Expose
    var originalLanguage: String? = null,
    @SerializedName("original_title")
    @Expose
    var originalTitle: String? = null,
    @SerializedName("overview")
    @Expose
    var overview: String? = null,
    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null,
    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null,
    @SerializedName("title")
    @Expose
    var title: String? = null,
    @SerializedName("video")
    @Expose
    var video: Boolean? = null,
    @SerializedName("vote_average")
    @Expose
    var voteAverage: Double? = null,
    @SerializedName("vote_count")
    @Expose
    var voteCount: Int? = null,
    @SerializedName("popularity")
    @Expose
    var popularity: Double? = null
) : Parcelable

data class MovieInfo(
    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String? = null,
    @SerializedName("genres")
    @Expose
    var genres: ArrayList<Genre> = arrayListOf(),
    @SerializedName("homepage")
    @Expose
    var homepage: String? = null,
    @SerializedName("id")
    @Expose
    var id: Int? = 0,
    @SerializedName("original_title")
    @Expose
    var originalTitle: String? = null,
    @SerializedName("overview")
    @Expose
    var overview: String? = null,
    @SerializedName("popularity")
    @Expose
    var popularity: Double = 0.0,
    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null,
    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null,
    @SerializedName("runtime")
    @Expose
    var runtime: Int? = null,
    @SerializedName("vote_average")
    @Expose
    var voteAverage: Double? = null,
    @SerializedName("vote_count")
    @Expose
    var voteCount: Int? = null
)

data class Genre(
    @SerializedName("name")
    @Expose
    var releaseDate: String? = null,
    @SerializedName("id")
    @Expose
    var runtime: Int? = null
)

val TMDB_IMAGE_PATH = "https://image.tmdb.org/t/p/original/"
val YOUTUBE_VIDEO_PATH = "https://www.youtube.com/watch?v="
