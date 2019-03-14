package com.example.cinemacrazy.di.modules

import android.content.Context
import com.example.cinemacrazy.BuildConfig
import com.example.cinemacrazy.apiservice.TmdbService
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class NetworkModule {

    @Provides
    @Singleton
    fun retrofit(
        okhttpClient: OkHttpClient,
        gson: GsonConverterFactory,
        callAdapterFactory: RxJava2CallAdapterFactory): TmdbService {

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_TMDB_API)
            .addConverterFactory(gson)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okhttpClient)
            .build().create(TmdbService::class.java)
    }

    @Provides
    @Singleton
    fun okHtppClient(interceptor: Interceptor, cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .cache(cache)
            .build()
    }

    @Provides
    @Singleton
    fun interceptor(): Interceptor {

        return Interceptor { chain ->
            chain.proceed(chain.request().newBuilder().build())
        }
    }

    @Provides
    @Singleton
    fun cache(context: Context): Cache {
        return Cache(context.cacheDir, 5 * 1024 * 1024)
    }


    @Provides
    @Singleton
    fun gson(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun adapterFactory(): RxJava2CallAdapterFactory{
        return RxJava2CallAdapterFactory.create()
    }

}