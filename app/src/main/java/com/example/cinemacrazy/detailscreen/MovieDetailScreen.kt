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
import com.example.cinemacrazy.datamodel.Image
import com.example.cinemacrazy.datamodel.TMDB_IMAGE_PATH
import com.example.cinemacrazy.datamodel.TrendingMovie
import com.example.cinemacrazy.datamodel.Video
import com.example.cinemacrazy.media.MOVIE_DETAIL
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.media_detail_screen.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MovieDetailScreen : BaseActivity(), AnkoLogger {

    var movie: TrendingMovie? = null
    lateinit var imageAdapter: MediaAdapter
    lateinit var videoAdapter: MediaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movie = intent.getParcelableExtra(MOVIE_DETAIL)
        media_recycler_images.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        media_recycler_videos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imageAdapter = MediaAdapter()
        videoAdapter = MediaAdapter()
        media_recycler_videos.adapter = videoAdapter
        media_recycler_images.adapter = imageAdapter

        info { "Movie null? ${movie == null}" }
        movie?.let {
            poppulateDetails(it)

            val movieDetailsViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel::class.java)
            it.id?.let { id ->

                movieDetailsViewModel.getSavedMoviesDetails(id.toInt(), api, database)
                movieDetailsViewModel.getImageLinks(movieId = id.toInt(), api = api)
                movieDetailsViewModel.getVideoLinks(movieId = id.toInt(), api = api)

                movieDetailsViewModel.mutableImageLinks.observe(this,
                    Observer<List<Image>?> { t -> imageAdapter.submitList(t) })

                movieDetailsViewModel.mutableVideoLinks.observe(this,
                    Observer<List<Video>?> { t -> videoAdapter.submitList(t) })
            }
        }

    }

    private fun poppulateDetails(movie: TrendingMovie) {

        val displayMetrics = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels * 2 / 3
        val params = CollapsingToolbarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)

        movie_image.layoutParams = params

        Glide.with(this)
            .applyDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.drawable.ic_launcher_foreground)
            )
            .load(movie.backdropPath?.TMDB_IMAGE_PATH())
            .into(movie_image)

        movie_overview.text = movie.overview

    }

    override fun getLayoutRes(): Int {
        return R.layout.media_detail_screen
    }
}