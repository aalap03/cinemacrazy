package com.example.cinemacrazy.medialist

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinemacrazy.R
import com.example.cinemacrazy.detailscreen.CinemaDetailScreen
import org.jetbrains.anko.AnkoLogger
import androidx.paging.PagedListAdapter
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.*
import com.example.cinemacrazy.datamodel.utils.CINEMA_TYPE_MOVIE
import com.example.cinemacrazy.datamodel.utils.CINEMA_TYPE_TV
import com.example.cinemacrazy.datamodel.utils.KEY_CINEMA_TYPE
import com.example.cinemacrazy.datamodel.utils.TMDB_IMAGE_PATH

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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.media_item, parent, false)
        return MovieHolder(view)
    }


    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        getItem(position)?.let { holder.bindItem(it) }
    }

    inner class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var moviePoster: ImageView = itemView.findViewById(R.id.movie_poster)

        fun bindItem(item: BaseMedia) {

            Glide.with(this.itemView.context)
                .load(item.posterPath()?.TMDB_IMAGE_PATH())
                .placeholder(R.drawable.ic_launcher_background)
                .into(moviePoster)

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