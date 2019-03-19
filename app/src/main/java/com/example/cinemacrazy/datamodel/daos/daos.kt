package com.example.cinemacrazy.datamodel.daos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cinemacrazy.datamodel.ImagePath
import com.example.cinemacrazy.datamodel.CinemaInfo
import com.example.cinemacrazy.datamodel.VideoPath

@Dao
interface CinemaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCinema(cinemaInfo: CinemaInfo)

    @Query("SELECT * FROM movie_info WHERE id = :movieId & cinemaType =:cinemaType")
    fun getCinema(movieId: Long, cinemaType: String): CinemaInfo?
}

@Dao
interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImages(listOfImages: MutableList<ImagePath>)

    @Query("SELECT * FROM ImagePath WHERE cinemaId = :cinemaId & cinemaType =:cinemaType")
    fun getImagesForCinema(cinemaId: Long, cinemaType: String): LiveData<MutableList<ImagePath>>?
}

@Dao
interface VideosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVideos(listOfVideos: MutableList<VideoPath>)

    @Query("SELECT * FROM VideoPath WHERE cinemaId = :cinemaId & cinemaType =:cinemaType")
    fun getVideosForCinema(cinemaId: Long, cinemaType: String): LiveData<MutableList<VideoPath>>?
}