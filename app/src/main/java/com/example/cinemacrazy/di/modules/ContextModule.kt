package com.example.cinemacrazy.di.modules

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.annotation.GlideModule
import com.example.cinemacrazy.application.App
import com.example.cinemacrazy.application.AppDb
import dagger.Module
import dagger.Provides


@Module
class ContextModule(var context: Context) {

    @Provides
    fun applicationContext(): Context{
        return context
    }

    @Provides
    fun application(): App{
        return App()
    }

    @Provides
    fun appDatabase(): AppDb {
        return AppDb.getDB(context)
    }
}