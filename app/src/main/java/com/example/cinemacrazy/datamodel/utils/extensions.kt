package com.example.cinemacrazy.datamodel.utils

import android.content.Context
import android.content.res.Resources
import androidx.core.content.ContextCompat
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.cinemacrazy.R

fun String.YOUTUBE_VIDEO_PATH() = "https://www.youtube.com/watch?v=$this"
fun String.TMDB_IMAGE_PATH(): String = "https://image.tmdb.org/t/p/w342/$this"

fun String.YOUTUBE_THUMBNAIL(): String =
    "https://img.youtube.com/vi/" +
            this.replace(".jpg", "").replace(".png", "").replace("/", "") +
            "/0.jpg"

fun Int.dpTopx(resources: Resources) = this * resources.displayMetrics.density

fun Int.getDrawable(context: Context) = ContextCompat.getDrawable(context, this)

fun RequestManager.getGlide(context: Context, resizedWidth: Int, loadString: String) =
        this.applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.gradient.getDrawable(context))
                .error(R.drawable.gradient.getDrawable(context))
                .override(resizedWidth, resizedWidth)
        ).load(loadString).override(resizedWidth, resizedWidth)