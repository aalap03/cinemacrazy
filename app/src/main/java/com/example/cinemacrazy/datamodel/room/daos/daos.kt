package com.example.cinemacrazy.datamodel.room.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cinemacrazy.datamodel.room.ImagePath
import com.example.cinemacrazy.datamodel.room.CinemaInfo
import com.example.cinemacrazy.datamodel.room.VideoPath

@Dao
interface CinemaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCinema(cinemaInfo: CinemaInfo)

    @Query("SELECT * FROM movie_info WHERE id = :movieId AND cinemaType = :cinemaType")
    fun getCinema(movieId: Long, cinemaType: String): CinemaInfo?

    @Query("SELECT * FROM movie_info")
    fun getAllCinema(): MutableList<CinemaInfo>
}

@Dao
interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImages(listOfImages: MutableList<ImagePath>)

    @Query("SELECT * FROM ImagePath WHERE cinemaId = :cinemaId AND cinemaType =:cinemaType")
    fun getImagesForCinema(cinemaId: Long, cinemaType: String): LiveData<MutableList<ImagePath>>?

    @Query("SELECT * FROM ImagePath")
    fun getAllImages(): MutableList<ImagePath>
}

@Dao
interface VideosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVideos(listOfVideos: MutableList<VideoPath>)

    @Query("SELECT * FROM VideoPath WHERE cinemaId = :cinemaId AND cinemaType =:cinemaType")
    fun getVideosForCinema(cinemaId: Long, cinemaType: String): LiveData<MutableList<VideoPath>>?

    @Query("SELECT * FROM VideoPath")
    fun getAllVideos(): MutableList<VideoPath>
}