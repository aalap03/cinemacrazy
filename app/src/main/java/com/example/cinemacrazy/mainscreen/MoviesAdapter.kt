package com.example.cinemacrazy.mainscreen

import android.app.Activity
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
import com.example.cinemacrazy.datamodel.TMDB_IMAGE_PATH
import com.example.cinemacrazy.datamodel.TrendingMovie
import com.example.cinemacrazy.detailscreen.MovieDetailScreen
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagedListAdapter

val POSTERPATH = "http://image.tmdb.org/t/p/w185/"
val MOVIE_DETAIL = "movie_details"

class MoviesAdapter: PagedListAdapter<TrendingMovie, MoviesAdapter.MovieHolder>(object: DiffUtil.ItemCallback<TrendingMovie>(){
    override fun areItemsTheSame(oldItem: TrendingMovie, newItem: TrendingMovie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TrendingMovie, newItem: TrendingMovie): Boolean {
        return oldItem.posterPath == newItem.posterPath
    }

}), AnkoLogger {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder =
        MovieHolder(LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false))

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    inner class MovieHolder( itemView: View): RecyclerView.ViewHolder(itemView) {

        var movieTitle : TextView = itemView.findViewById(R.id.movie_title)
        var movieContent : TextView = itemView.findViewById(R.id.movie_content)
        var moviePoster : ImageView = itemView.findViewById(R.id.movie_poster)

        fun bindItem(item: TrendingMovie?) {
            movieContent.text = item?.overview
            movieTitle.text = item?.title

            info{"Item: $item"}

            Glide.with(itemView.context)
                .load(item?.posterPath?.TMDB_IMAGE_PATH())
                .into(moviePoster)

            itemView.setOnClickListener {

                val options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(((itemView.context) as Activity), moviePoster as View, "poster")

                val intent = Intent(itemView.context, MovieDetailScreen::class.java)
                intent.putExtra(MOVIE_DETAIL, item)
                itemView.context.startActivity(intent, options.toBundle())
            }
        }

    }
}