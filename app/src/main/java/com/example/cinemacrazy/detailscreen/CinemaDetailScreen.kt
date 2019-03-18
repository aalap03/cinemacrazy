package com.example.cinemacrazy.detailscreen

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cinemacrazy.BaseActivity
import com.example.cinemacrazy.R
import com.example.cinemacrazy.datamodel.*
import com.example.cinemacrazy.medialist.MOVIE_DETAIL
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.media_detail_screen.*
import org.jetbrains.anko.AnkoLogger

class CinemaDetailScreen : BaseActivity(), AnkoLogger {

    var movie: TrendingMovie? = null
    var tv: TrendingTv? = null
    var mediaType = ""
    lateinit var baseMedia: BaseMedia
    lateinit var detailsViewModel: CinemaDetailsViewModel
    var imageAdapter = MediaAdapter()
    var videoAdapter = MediaAdapter()
    var serverImageList = arrayListOf<MovieMedia>()
    var serverVideoList = mutableListOf<MovieMedia>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recycler_view_images.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_images.adapter = imageAdapter
        recycler_view_videos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_videos.adapter = videoAdapter

        mediaType = intent.getStringExtra(KEY_CINEMA_TYPE)
        if (mediaType == CINEMA_TYPE_MOVIE)
            movie = intent.getParcelableExtra(MOVIE_DETAIL)
        else
            tv = intent.getParcelableExtra(MOVIE_DETAIL)

        movie?.let { baseMedia = movie as BaseMedia }
        tv?.let { baseMedia = tv as BaseMedia }

        showMediaStoredDetails(baseMedia)
        detailsViewModel = ViewModelProviders.of(this).get(CinemaDetailsViewModel::class.java)

        detailsViewModel.getSavedMoviesDetails(baseMedia.getMediaId(), api, database)

        detailsViewModel.movieInfo.observe(this,
            Observer<MovieInfo?> {
                it?.images
            })


    }

    private fun showMediaStoredDetails(movie: BaseMedia) {

        val displayMetrics = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels  / 3
        val params = CollapsingToolbarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)

        movie_image.layoutParams = params

        Glide.with(this)
            .applyDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.drawable.ic_launcher_foreground)
            )
            .load(movie.backdropPath()?.TMDB_IMAGE_PATH())
            .into(movie_image)

        movie_overview.text = movie.overView()

    }

    override fun getLayoutRes(): Int {
        return R.layout.media_detail_screen
    }
}