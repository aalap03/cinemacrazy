package com.example.cinemacrazy.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("key")
    @Expose
    var key: String = "",
    @SerializedName("type")
    @Expose
    var type: String = "",
    @SerializedName("site")
    @Expose
    var site: String = ""
) : MovieMedia {
    override fun getLinkKey(): String {
        return key
    }

    override fun mediaType(): String {
        return VIDEO
    }
}

data class VideoResult(
    @SerializedName("results")
    @Expose
    var videos: MutableList<Video> = mutableListOf()
)

data class Image(
    @SerializedName("file_path")
    @Expose
    var filePath: String = ""
) : MovieMedia {
    override fun getLinkKey(): String {
        return filePath
    }

    override fun mediaType(): String {
        return IMAGE
    }
}

data class ImageResult(
    @SerializedName("backdrops")
    @Expose
    var images: MutableList<Image> = arrayListOf()
)

interface MovieMedia {
    fun getLinkKey(): String
    fun mediaType(): String
}

val IMAGE = "image"
val VIDEO = "video"