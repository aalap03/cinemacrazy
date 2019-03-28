package com.example.cinemacrazy.detailscreen

import android.content.Intent
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
import com.example.cinemacrazy.datamodel.serverResponses.mediaResponses.MovieMedia
import com.example.cinemacrazy.datamodel.room.CinemaInfo
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.BaseMedia
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.TrendingMovie
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.TrendingTv
import com.example.cinemacrazy.datamodel.utils.constant.CINEMA_TYPE_MOVIE
import com.example.cinemacrazy.datamodel.utils.constant.KEY_CINEMA_TYPE
import com.example.cinemacrazy.datamodel.utils.TMDB_IMAGE_PATH
import com.example.cinemacrazy.medialist.MOVIE_DETAIL
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.cinema_details.*
import kotlinx.android.synthetic.main.media_detail_screen.*
import org.jetbrains.anko.AnkoLogger
import android.graphics.Paint
import com.example.cinemacrazy.R


class CinemaDetailScreen : BaseActivity(), AnkoLogger {

    var movie: TrendingMovie? = null
    var tv: TrendingTv? = null
    var mediaType = ""
    lateinit var baseMedia: BaseMedia
    lateinit var detailsViewModel: CinemaDetailsViewModel
    lateinit var genres: String
    var imageAdapter = MediaAdapter()
    var videoAdapter = MediaAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mediaType = intent.getStringExtra(KEY_CINEMA_TYPE)

        setMediaRecyclerViews()
        setGenresText()

        movie?.let { baseMedia = movie as BaseMedia }
        tv?.let { baseMedia = tv as BaseMedia }

        showMediaStoredDetails(baseMedia)//votes, release date, overview
        detailsViewModel = ViewModelProviders.of(this).get(CinemaDetailsViewModel::class.java)

        detailsViewModel.requestCinemaDetails(baseMedia.getMediaId(), api, database, baseMedia.mediaType())

        //homepage, runtime
        detailsViewModel.liveCinemaInfo.observe(this,
            Observer<CinemaInfo?> { cinemaInfo ->
                cinemaInfo?.homePageLink?.let { cinemaHomePage ->
                    cinema_homepage.visibility = View.VISIBLE
                    cinema_homepage_heading.visibility = View.VISIBLE
                    cinema_homepage.paintFlags = Paint.UNDERLINE_TEXT_FLAG

                    cinema_homepage.text = cinemaHomePage
                    cinema_homepage.setOnClickListener {
                        val intent = Intent(this@CinemaDetailScreen, WebView::class.java)
                        intent.putExtra(HOME_PAGE, cinemaHomePage)
                        intent.putExtra(TITLE, baseMedia.getName())
                        startActivity(intent)
                    }
                }
                val runTimeMinutes = cinemaInfo?.runTimeMinutes
                runTimeMinutes?.let {
                    val div = runTimeMinutes.div(60)
                    val rem = runTimeMinutes.rem(60)
                    if (div > 0) {
                        cinema_runtime.text = "$div hours $rem minutes"
                    } else
                        cinema_runtime.text = "$rem minutes"
                }

            })

        detailsViewModel.cinemaImages.observe(
            this,
            Observer<MutableList<MovieMedia>?> { t -> imageAdapter.submitList(t) })
        detailsViewModel.imagesLoading.observe(
            this,
            Observer<Boolean?> { image_loading.visibility = if (it == true) View.VISIBLE else View.GONE })
        detailsViewModel.cinemaVideos.observe(
            this,
            Observer<MutableList<MovieMedia>?> { videos -> videoAdapter.submitList(videos) })
        detailsViewModel.videosLoading.observe(
            this,
            Observer<Boolean?> { videos_loading.visibility = if (it == true) View.VISIBLE else View.GONE })
    }

    private fun setGenresText() {
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
        cinema_genre.text = genres.replace("[", "").replace("]", "")
    }

    private fun setMediaRecyclerViews() {
        recycler_view_images.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_images.adapter = imageAdapter
        recycler_view_videos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_videos.adapter = videoAdapter
    }

    private fun showMediaStoredDetails(movie: BaseMedia) {

        cinema_release_date.text = movie.relaeseDate()
        cinema_votes.text = "TMDB ${movie.voteAvrg()} /10"
        cinema_storyline.text = movie.overView()

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

        Glide.with(this)
            .applyDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.drawable.ic_launcher_foreground)
            )
            .load(movie.posterPath()?.TMDB_IMAGE_PATH())
            .into(cinema_poster)
    }

    override fun getLayoutRes(): Int {
        return R.layout.media_detail_screen
    }
}