package com.example.cinemacrazy.datamodel.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cinemacrazy.datamodel.MovieInfo

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movieInfo: MovieInfo)

    @Query("SELECT * FROM movie_info WHERE id = :movieId")
    fun getMovie(movieId: Int): MovieInfo
}