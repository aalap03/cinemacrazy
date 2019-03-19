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
    var cinemaId: Long,
    var cinemaType: String
): MovieMedia {
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
): MovieMedia {
    override fun getLinkKey(): String {
        return key
    }

    override fun mediaType(): String {
        return VIDEO
    }
}

class Convertors {

    companion object {

        @TypeConverter
        @JvmStatic
        fun imagesToString(images: MutableList<ImagePath>?): String? {
            var gson = Gson()
            return gson.toJson(images)
        }

        @TypeConverter
        @JvmStatic
        fun stringToImagePaths(imagesAsString: String?): MutableList<ImagePath>? {
            var gson = Gson()
            return gson.fromJson<MutableList<ImagePath>>(imagesAsString, ImagePath::class.java)
        }

        @TypeConverter
        @JvmStatic
        fun videosToString(videos: MutableList<VideoPath>?): String? {
            var gson = Gson()
            return gson.toJson(videos)
        }

        @TypeConverter
        @JvmStatic
        fun stringToVideoPaths(videosAsString: String?): MutableList<VideoPath>? {
            var gson = Gson()
            return gson.fromJson<MutableList<VideoPath>>(videosAsString, VideoPath::class.java)
        }
    }

}
