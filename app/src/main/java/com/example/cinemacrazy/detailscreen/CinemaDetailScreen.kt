package com.example.cinemacrazy.detailscreen

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cinemacrazy.BaseActivity
import com.example.cinemacrazy.R
import com.example.cinemacrazy.application.App
import com.example.cinemacrazy.datamodel.*
import com.example.cinemacrazy.medialist.MOVIE_DETAIL
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.media_detail_screen.*
import org.jetbrains.anko.AnkoLogger

class CinemaDetailScreen : BaseActivity(), AnkoLogger {

    lateinit var app: App
    var movie: TrendingMovie? = null
    var tv: TrendingTv? = null
    var mediaType = ""
    lateinit var baseMedia: BaseMedia
    lateinit var detailsViewModel: CinemaDetailsViewModel
    lateinit var genres: String
    var imageAdapter = MediaAdapter()
    var videoAdapter = MediaAdapter()
    var serverImageList = arrayListOf<MovieMedia>()
    var serverVideoList = mutableListOf<MovieMedia>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = App()

        recycler_view_images.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_images.adapter = imageAdapter
        recycler_view_videos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_videos.adapter = videoAdapter

        mediaType = intent.getStringExtra(KEY_CINEMA_TYPE)
        if (mediaType == CINEMA_TYPE_MOVIE) {
            movie = intent.getParcelableExtra(MOVIE_DETAIL)
            val arrayList = movie?.genreIds()
            arrayList?.let {
                genres = app.getMoviesGenre(it).toString()
            }
        } else {
            tv = intent.getParcelableExtra(MOVIE_DETAIL)
            val arrayList = tv?.genreIds()
            arrayList?.let {
                genres = app.getTvGenres(it).toString()
            }
        }

        movie?.let { baseMedia = movie as BaseMedia }
        tv?.let { baseMedia = tv as BaseMedia }

        showMediaStoredDetails(baseMedia)
        detailsViewModel = ViewModelProviders.of(this).get(CinemaDetailsViewModel::class.java)

        detailsViewModel.requestCinemaDetails(baseMedia.getMediaId(), api, database, baseMedia.mediaType())

        detailsViewModel.liveCinemaInfo.observe(this,
            Observer<CinemaInfo?> { cinemaInfo ->
                cinema_home_page.text = cinemaInfo?.homePageLink
                cinema_runtime.text = "${cinemaInfo?.runTimeMinutes}"
            })

        detailsViewModel.liveImagePaths.observe(this,
            Observer<MutableList<MovieMedia>?> { t -> imageAdapter.submitList(t) })

        detailsViewModel.liveVideoPaths.observe(this,
            Observer<MutableList<MovieMedia>?> { videos -> videoAdapter.submitList(videos) })

        detailsViewModel.imagesLoading.observe(this, Observer<Boolean?> {
            image_loading.visibility = if (it == true) View.VISIBLE else View.GONE
        })

        detailsViewModel.videosLoading.observe(this, Observer<Boolean?> {
            videos_loading.visibility = if (it == true) View.VISIBLE else View.GONE
        })


    }

    //DONE PART
    private fun showMediaStoredDetails(movie: BaseMedia) {

        val displayMetrics = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels / 3
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
        movie_genre.text = genres

    }

    override fun getLayoutRes(): Int {
        return R.layout.media_detail_screen
    }
}