package com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

interface BaseCinemaDetail {
    fun homepage(): String?
    fun runtime(): Int
    fun genre(): ArrayList<Genre>
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

data class Genre(
    @SerializedName("name")
    @Expose
    var name: String = "",
    @SerializedName("id")
    @Expose
    var id: Long = 0
)
