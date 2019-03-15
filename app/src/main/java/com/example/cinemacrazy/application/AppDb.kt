package com.example.cinemacrazy.application

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cinemacrazy.datamodel.MovieInfo
import com.example.cinemacrazy.datamodel.daos.MovieDao

@Database(version = 1, entities = [MovieInfo::class])
abstract class AppDb: RoomDatabase() {

    abstract fun moviesDao(): MovieDao

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