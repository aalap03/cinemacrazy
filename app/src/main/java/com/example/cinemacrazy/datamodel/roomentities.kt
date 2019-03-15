package com.example.cinemacrazy.datamodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_info")
data class MovieInfo(

    @PrimaryKey
    var id: Long = 0,

    var runTimeMinutes: Int = 0,

    var homePageLink: String? = null

//    var genres: ArrayList<String> = arrayListOf(),

//    var videos: ArrayList<String> = arrayListOf(),
//
//    var images: ArrayList<String> = arrayListOf()
)