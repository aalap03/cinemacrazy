package com.example.cinemacrazy.datamodel.utils

import android.content.Context
import android.content.res.Resources
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.cinemacrazy.R

fun String.TMDB_POSTER_IMAGE_PATH(): String = "https://image.tmdb.org/t/p/w342/$this"
fun String.TMDB_POSTER_IMAGE_PATH_ORIGINAL(): String = "https://image.tmdb.org/t/p/original/$this"

fun String.TMDB_BACKDROP_IMAGE_PATH(): String = "https://image.tmdb.org/t/p/original/$this"

//not useful if you are using YoutubePlayerView
fun String.YOUTUBE_VIDEO_PATH() = "https://www.youtube.com/watch?v=$this"
fun String.YOUTUBE_THUMBNAIL(): String =
    "https://img.youtube.com/vi/" +
            this.replace(".jpg", "").replace(".png", "").replace("/", "") +
            "/0.jpg"

fun Int.dpTopx(resources: Resources) = this * resources.displayMetrics.density

fun Int.getDrawable(context: Context) = ContextCompat.getDrawable(context, this)

fun RequestManager.loadImage(url: String, targetView: ImageView) =
    this.applyDefaultRequestOptions(RequestOptions().error(R.drawable.tmdb_logo_image).placeholder(R.drawable.tmdb_logo_image))
        .load(url).into(targetView)

