package com.example.cinemacrazy.detailscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cinemacrazy.R
import com.example.cinemacrazy.datamodel.utils.TMDB_POSTER_IMAGE_PATH_ORIGINAL
import org.jetbrains.anko.AnkoLogger


class ImagesPagerAdapter(var movies: MutableList<String>) : RecyclerView.Adapter<ImagesPagerAdapter.MovieHolder>(), AnkoLogger {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder =
        MovieHolder(LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false))

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieHolder, position: Int) = holder.bindIten(movies[position])


    inner class MovieHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var posterImage : ImageView = view.findViewById(R.id.image_item)

        fun bindIten(movieItem: String) {
            Glide.with(view.context)
                .applyDefaultRequestOptions(RequestOptions().error(R.drawable.tmdb_logo_image).placeholder(R.drawable.tmdb_logo_image))
                .load(movieItem.TMDB_POSTER_IMAGE_PATH_ORIGINAL())
                .into(posterImage)
        }
    }
}