package com.example.cinemacrazy.datamodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "movie_info")
data class MovieInfo(

    @PrimaryKey
    var id: Long = 0,

    var runTimeMinutes: Int = 0,

    var homePageLink: String? = null,

    var genres: MutableList<MediaGenres> = arrayListOf(),

    var videos: MutableList<VideoPath> = arrayListOf(),

    var images: MutableList<ImagePath> = arrayListOf()
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

@Entity
data class MediaGenres(

    @PrimaryKey
    var id: Int = 0,

    var genre: String = "",
    var cinemaId: Long
)

class Convertors {

    companion object {
        @TypeConverter
        @JvmStatic
        fun toGenresString(genres: MutableList<MediaGenres>): String {
            return genres.joinToString(separator = ",") {
                "${it.genre}:${it.id}"
            }
        }

        @TypeConverter
        @JvmStatic
        fun toGenreList(genreString: String): MutableList<MediaGenres> {
            return genreString.split(",").map {
                val split = it.split(":")
                MediaGenres(split[1].toInt(), split[0],0)
            }.toMutableList()
        }

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
