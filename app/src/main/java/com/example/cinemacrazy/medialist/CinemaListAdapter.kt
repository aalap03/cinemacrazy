package com.example.cinemacrazy.medialist

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinemacrazy.R
import com.example.cinemacrazy.detailscreen.CinemaDetailScreen
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import androidx.paging.PagedListAdapter
import androidx.palette.graphics.Palette
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.cinemacrazy.application.getColor
import com.example.cinemacrazy.datamodel.*

val POSTERPATH = "http://image.tmdb.org/t/p/w185/"
val MOVIE_DETAIL = "movie_details"

class MoviesAdapter :
    PagedListAdapter<BaseMedia, MoviesAdapter.MovieHolder>(object : DiffUtil.ItemCallback<BaseMedia>() {
        override fun areItemsTheSame(oldItem: BaseMedia, newItem: BaseMedia): Boolean {
            return oldItem.getMediaId() == newItem.getMediaId()
        }

        override fun areContentsTheSame(oldItem: BaseMedia, newItem: BaseMedia): Boolean {
            return oldItem.posterPath() == newItem.posterPath()
        }

    }), AnkoLogger {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder =
        MovieHolder(LayoutInflater.from(parent.context).inflate(R.layout.media_item, parent, false))

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        getItem(position)?.let { holder.bindItem(it) }
    }

    inner class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var moviePoster: ImageView = itemView.findViewById(R.id.movie_poster)
        var cinemaTitle: TextView = itemView.findViewById(R.id.cinema_title)
        var titleContainer: FrameLayout = itemView.findViewById(R.id.title_container)

        fun bindItem(item: BaseMedia) {

            val width = itemView.resources.displayMetrics.widthPixels / 2

            info { "Item: $item" }

            itemView.layoutParams.width = width
            itemView.layoutParams.height = (width + 40.dpTopx(itemView.resources)).toInt()

            Glide.with(itemView.context)
                .getGlide(itemView.context,
                    width,
                    item.posterPath()?.TMDB_IMAGE_PATH() ?: ""
                )
                .addListener(object: RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        val bitmap = (resource as BitmapDrawable).bitmap
                        moviePoster.setImageBitmap(bitmap)
                        Palette.from(bitmap).generate { palette ->

                            val darkVibrantColor = palette?.getDarkVibrantColor(R.color.colorAccent.getColor(itemView.context))
                            darkVibrantColor?.let { titleContainer.setBackgroundColor(it) }

                        }
                        return true
                    }
                })
                .into(moviePoster)


            cinemaTitle.text = item.getName()

            itemView.setOnClickListener {
                val trendingTv = if (item.mediaType() == CINEMA_TYPE_TV) item as TrendingTv else null
                val trendingMovie = if (item.mediaType() == CINEMA_TYPE_MOVIE) item as TrendingMovie else null

                val intent = Intent(itemView.context, CinemaDetailScreen::class.java)
                intent.putExtra(KEY_CINEMA_TYPE, item.mediaType())

                trendingMovie?.let { intent.putExtra(MOVIE_DETAIL, it) }
                trendingTv?.let { intent.putExtra(MOVIE_DETAIL, it) }

                itemView.context.startActivity(intent)
            }
        }

    }
}