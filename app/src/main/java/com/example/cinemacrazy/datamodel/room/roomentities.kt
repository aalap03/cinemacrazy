package com.example.cinemacrazy.datamodel.room

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.cinemacrazy.datamodel.serverResponses.mediaResponses.IMAGE
import com.example.cinemacrazy.datamodel.serverResponses.mediaResponses.MovieMedia
import com.example.cinemacrazy.datamodel.serverResponses.mediaResponses.VIDEO
import com.google.gson.Gson
import java.lang.reflect.Type
import com.google.gson.reflect.TypeToken


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
    var cinemaId: Long,
    var cinemaType: String
) : MovieMedia {
    override fun getLinkKey(): String {
        return key
    }

    override fun mediaType(): String {
        return IMAGE
    }
}

@Entity
data class VideoPath(
    @PrimaryKey
    var key: String = "",
    var cinemaId: Long,
    var cinemaType: String
) : MovieMedia {
    override fun getLinkKey(): String {
        return key
    }

    override fun mediaType(): String {
        return VIDEO
    }
}

class Convertors {

    companion object {

        var gson = Gson()
        var TAG = "Convertors:"

        @TypeConverter
        @JvmStatic
        fun imagesToString(images: MutableList<ImagePath>?): String? {
            val type = object : TypeToken<MutableList<ImagePath>>() {}.type
            return gson.toJson(images, type)
        }

        @TypeConverter
        @JvmStatic
        fun stringToImagePaths(imagesAsString: String?): MutableList<ImagePath>? {
            Log.d(TAG, "Images: $imagesAsString")
            val type = object : TypeToken<MutableList<ImagePath>>() {}.type
            return gson.fromJson<MutableList<ImagePath>>(imagesAsString, type)
        }

        @TypeConverter
        @JvmStatic
        fun videosToString(videos: MutableList<VideoPath>?): String? {
            val type = object : TypeToken<MutableList<VideoPath>>() {}.type
            return gson.toJson(videos, type)
        }

        @TypeConverter
        @JvmStatic
        fun stringToVideoPaths(videosAsString: String?): MutableList<VideoPath>? {
            Log.d(TAG, "Videos: $videosAsString")
            val type = object : TypeToken<MutableList<VideoPath>>() {}.type
            return gson.fromJson<MutableList<VideoPath>>(videosAsString, type)
        }
    }

}
