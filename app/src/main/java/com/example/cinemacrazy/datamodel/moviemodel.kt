package com.example.cinemacrazy.datamodel

import android.os.Parcelable
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
data class TrendingMovie(

    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String = "",
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
    var posterPath: String = "",
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

    override fun backdropPath(): String {
        return backdropPath
    }

    override fun posterPath(): String {
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

    override fun relaeseDate(): String {
        return releaseDate
    }

    override fun overView(): String {
        return overview
    }
}

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
    var posterPath: String = "",
    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String = "",
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

    override fun backdropPath(): String {
        return backdropPath
    }

    override fun posterPath(): String {
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

interface BaseMedia {

    fun mediaType(): String

    fun getMediaId(): Long

    fun getName(): String

    fun backdropPath(): String?

    fun posterPath(): String?

    fun genreIds(): ArrayList<Long>

    fun voteCount(): Long

    fun voteAvrg(): Double

    fun relaeseDate(): String

    fun overView(): String
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
):BaseCinemaDetail {
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

interface BaseCinemaDetail {
    fun homepage(): String?
    fun runtime(): Int
    fun genre(): ArrayList<Genre>
}

data class Genre(
    @SerializedName("name")
    @Expose
    var name: String = "",
    @SerializedName("id")
    @Expose
    var id: Long = 0
)

val CINEMA_TYPE_TV = "tv"
val CINEMA_TYPE_MOVIE = "movie"
val KEY_CINEMA_TYPE = "cinema_type"
val KEY_CINEMA_ID = "cinema_type"

val MEDIA_TYPE_IMAGES = "images"
val MEDIA_TYPE_VIDEOS = "videos"
val KEY_MEDIA_TYPE = "media_type"
val KEY_MEDIA_IDS = "media_ids"

fun String.YOUTUBE_VIDEO_PATH() = "https://www.youtube.com/watch?v=$this"
fun String.TMDB_IMAGE_PATH(): String = "https://image.tmdb.org/t/p/original/$this"

fun String.YOUTUBE_THUMBNAIL(): String =
    "https://img.youtube.com/vi/" +
            this.replace(".jpg", "").replace(".png", "").replace("/", "") +
            "/0.jpg"

