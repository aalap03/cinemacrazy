package com.example.cinemacrazy.application

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cinemacrazy.datamodel.room.CinemaInfo
import com.example.cinemacrazy.datamodel.room.Convertors
import com.example.cinemacrazy.datamodel.room.ImagePath
import com.example.cinemacrazy.datamodel.room.VideoPath
import com.example.cinemacrazy.datamodel.room.daos.ImagesDao
import com.example.cinemacrazy.datamodel.room.daos.CinemaDao
import com.example.cinemacrazy.datamodel.room.daos.VideosDao

@Database(version = 4, entities = [CinemaInfo::class, ImagePath::class, VideoPath::class])
@TypeConverters(Convertors::class)
abstract class AppDb: RoomDatabase() {

    abstract fun cinemaDao(): CinemaDao
    abstract fun imagesDao(): ImagesDao
    abstract fun videosDao(): VideosDao

    companion object {
        @Volatile private var INSTANCE: AppDb? = null

        fun getDB(context: Context): AppDb{

            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context):AppDb {
            return Room.databaseBuilder(context.applicationContext, AppDb::class.java, "movies-db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}