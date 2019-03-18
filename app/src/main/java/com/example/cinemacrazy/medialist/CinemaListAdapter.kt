package com.example.cinemacrazy.medialist

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinemacrazy.R
import com.example.cinemacrazy.detailscreen.CinemaDetailScreen
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import androidx.paging.PagedListAdapter
import com.bumptech.glide.request.RequestOptions
import com.example.cinemacrazy.datamodel.*

val POSTERPATH = "http://image.tmdb.org/t/p/w185/"
val MOVIE_DETAIL = "movie_details"

class MoviesAdapter: PagedListAdapter<BaseMedia, MoviesAdapter.MovieHolder>(object: DiffUtil.ItemCallback<BaseMedia>(){
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

    inner class MovieHolder( itemView: View): RecyclerView.ViewHolder(itemView) {

        var movieTitle : TextView = itemView.findViewById(R.id.movie_title)
        var movieContent : TextView = itemView.findViewById(R.id.movie_content)
        var moviePoster : ImageView = itemView.findViewById(R.id.movie_poster)

        fun bindItem(item: BaseMedia) {
            movieContent.text = item.overView()
            movieTitle.text = item.getName()

            info{"Item: $item"}

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(RequestOptions().placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_foreground))
                .load(item.posterPath()?.TMDB_IMAGE_PATH() ?: "")
                .into(moviePoster)

            itemView.setOnClickListener {
                val trendingTv = if(item.mediaType() == CINEMA_TYPE_TV) item as TrendingTv else null
                val trendingMovie = if(item.mediaType() == CINEMA_TYPE_MOVIE) item as TrendingMovie else null

                val intent = Intent(itemView.context, CinemaDetailScreen::class.java)
                intent.putExtra(KEY_CINEMA_TYPE, item.mediaType())

                trendingMovie?.let { intent.putExtra(MOVIE_DETAIL, it) }
                trendingTv?.let { intent.putExtra(MOVIE_DETAIL, it) }

                itemView.context.startActivity(intent)
            }
        }

    }
}