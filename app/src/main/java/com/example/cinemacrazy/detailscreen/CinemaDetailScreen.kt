package com.example.cinemacrazy.detailscreen

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.example.cinemacrazy.datamodel.utils.TMDB_POSTER_IMAGE_PATH
import com.example.cinemacrazy.medialist.MOVIE_DETAIL
import kotlinx.android.synthetic.main.cinema_details.*
import kotlinx.android.synthetic.main.media_detail_screen.*
import org.jetbrains.anko.AnkoLogger
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import androidx.palette.graphics.Palette
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.cinemacrazy.R
import com.example.cinemacrazy.application.getColor
import com.example.cinemacrazy.datamodel.utils.TMDB_BACKDROP_IMAGE_PATH

val KEY_POSITION = "position"
val KEY_IMAGE_LIST = "key_image_list"

class CinemaDetailScreen : BaseActivity(), AnkoLogger, MediaClickCallback {

    var movie: TrendingMovie? = null
    var tv: TrendingTv? = null
    var mediaType = ""
    lateinit var baseMedia: BaseMedia
    lateinit var detailsViewModel: CinemaDetailsViewModel
    lateinit var genres: String
    var imageAdapter = MediaAdapter(this)
    var videoAdapter = MediaAdapter(this)
    private val imageLinkList = arrayListOf<String>()
    private val videoLinkList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mediaType = intent.getStringExtra(KEY_CINEMA_TYPE)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toolbar.navigationIcon?.setColorFilter(R.color.white.getColor(this), PorterDuff.Mode.SRC_ATOP)

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
                    cinema_homepage_heading.visibility = View.VISIBLE
                    cinema_homepage_heading.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                    cinema_homepage_heading.setOnClickListener {
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
            Observer<MutableList<MovieMedia>?> { imageLinks ->
                imageAdapter.submitList(imageLinks)
                cinema_images_heading.text = "Images (${imageLinks?.size})"
                imageLinks?.forEach { movieMedia -> imageLinkList.add(movieMedia.getLinkKey()) }
            })
        detailsViewModel.imagesLoading.observe(
            this,
            Observer<Boolean?> { image_loading.visibility = if (it == true) View.VISIBLE else View.GONE })
        detailsViewModel.cinemaVideos.observe(
            this,
            Observer<MutableList<MovieMedia>?> { videos ->
                videoAdapter.submitList(videos)
                cinema_videos_heading.text = "Video (${videos?.size})"
                videos?.forEach { movieMedia -> videoLinkList.add(movieMedia.getLinkKey()) }
            })
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
            arrayList?.let { genres = app.getTvGenres(it).toString() }
        }

        if (TextUtils.isEmpty(genres))
            cinema_genre.text = "Unknown Genre"
        else
            cinema_genre.text = genres.replace("[", "").replace("]", "")
    }

    private fun setMediaRecyclerViews() {
        recycler_view_images.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_images.adapter = imageAdapter
        recycler_view_videos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_videos.adapter = videoAdapter
    }

    private fun showMediaStoredDetails(movie: BaseMedia) {

        cinema_release_date.text = movie.releaseDate()
        cinema_votes.text = "TMDB ${movie.voteAvrg()} /10 (${movie.voteCount()} Votes)"
        cinema_storyline.text = movie.overView()
        collapsing_toolbar.title = movie.getName()

        Glide.with(this)
            .applyDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.tmdb_logo_image)
                    .error(R.drawable.tmdb_logo_image)
            )
            .load(movie.backdropPath()?.TMDB_BACKDROP_IMAGE_PATH())
            .addListener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    collapsing_toolbar.setStatusBarScrimColor(R.color.colorAccent.getColor(this@CinemaDetailScreen))
                    collapsing_toolbar.setContentScrimColor(R.color.colorPrimary.getColor(this@CinemaDetailScreen))
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (resource is BitmapDrawable) {
                        Palette.from(resource.bitmap)
                            .generate { palette ->
                                val vibrant = palette?.vibrantSwatch
                                val mutedColor = palette?.vibrantSwatch?.rgb
                                if (vibrant != null) {
                                    collapsing_toolbar.setStatusBarScrimColor(
                                        palette.getDarkMutedColor(
                                            mutedColor ?: R.color.colorAccent
                                        )
                                    )
                                    collapsing_toolbar.setContentScrimColor(
                                        palette.getMutedColor(
                                            mutedColor ?: R.color.colorPrimary
                                        )
                                    )
                                }
                            }
                    }
                    return false
                }
            })
            .into(movie_image)

        Glide.with(this)
            .applyDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.tmdb_logo_image)
                    .error(R.drawable.tmdb_logo_image)
            )
            .load(movie.posterPath()?.TMDB_POSTER_IMAGE_PATH())
            .into(cinema_poster)
    }

    override fun mediaClicked(position: Int, mediaKey: String, isMediaImage: Boolean) {
        if (isMediaImage) {
            val intent = Intent(this, ImageScreen::class.java)
            intent.putExtra(KEY_POSITION, position)
            intent.putExtra(KEY_IMAGE_LIST, imageLinkList)
            startActivity(intent)
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                openPiP()
            else
                watchYoutubeVideo(mediaKey)
        }
    }

    private fun openPiP() {
        val intent = Intent(this, PictureInPictureScreen::class.java)
        intent.putExtra(KEY_VIDEO_LINKS, videoLinkList)
        startActivity(intent)
    }

    private fun watchYoutubeVideo(id: String) {

        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$id"))
        try {
            startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            startActivity(webIntent)
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.media_detail_screen
    }
}