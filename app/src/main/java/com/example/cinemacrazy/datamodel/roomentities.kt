package com.example.cinemacrazy.datamodel

import androidx.lifecycle.MutableLiveData
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

@Entity(tableName = "movie_info")
data class CinemaInfo(

    @PrimaryKey
    var id: Long = 0,

    var cinemaType: String = "",

    var runTimeMinutes: Int = 0,

    var homePageLink: String? = null,

    var videos: MutableList<VideoPath>? = null,

    var images: MutableList<ImagePath>? = null
)

@Entity
data class ImagePath(
    @PrimaryKey
    var key: String = "",
    var cinemaId: Long
)

@Entity
data class VideoPath(
    @PrimaryKey
    var key: String = "",
    var cinemaId: Long
)

class Convertors {

    companion object {

        @TypeConverter
        @JvmStatic
        fun toImagePathToString(imgePaths: MutableList<ImagePath>): String {
            return imgePaths.joinToString(separator = ",") { "${it.key}:${it.cinemaId}" }
        }

        @TypeConverter
        @JvmStatic
        fun toImageList(imagePathStrings: String): MutableList<ImagePath> {
            return imagePathStrings.split(",").map {
                val split = it.split(":")
                ImagePath(split[0], split[1].toLong())
            }.toMutableList()
        }

        @TypeConverter
        @JvmStatic
        fun toVideoPathToString(videoPathString: MutableList<VideoPath>): String {
            return videoPathString.joinToString(separator = ",") { "${it.key}:${it.cinemaId}" }
        }

        @TypeConverter
        @JvmStatic
        fun toVideoList(genreString: String): MutableList<VideoPath> {
            return genreString.split(",").map {
                val split = it.split(":")
                VideoPath(split[0], split[1].toLong())
            }.toMutableList()
        }
    }

}
