package com.example.cinemacrazy.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides


@Module
class ContextModule(var context: Context) {

    @Provides
    fun applicationContext(): Context{
        return context
    }

}