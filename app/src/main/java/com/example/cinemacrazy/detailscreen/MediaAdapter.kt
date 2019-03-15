package com.example.cinemacrazy.detailscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cinemacrazy.R
import com.example.cinemacrazy.datamodel.*

class MediaAdapter : ListAdapter<MovieMedia, MediaAdapter.MediaHolder>(object : DiffUtil.ItemCallback<MovieMedia>() {

    override fun areItemsTheSame(oldItem: MovieMedia, newItem: MovieMedia): Boolean {
        return oldItem.getLinkKey() == newItem.getLinkKey()
    }

    override fun areContentsTheSame(oldItem: MovieMedia, newItem: MovieMedia): Boolean {
        return oldItem.getLinkKey() == newItem.getLinkKey()
    }

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaHolder =
        MediaHolder(LayoutInflater.from(parent.context).inflate(R.layout.simple_image_card_item, parent, false))

    override fun onBindViewHolder(holder: MediaHolder, position: Int) {
        holder.bindView(getItem(position))
    }

    inner class MediaHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var cardImage: ImageView = view.findViewById(R.id.simple_card_image)

        fun bindView(media: MovieMedia) {

            if (media.mediaType() == IMAGE) {
                Glide.with(view.context)
                    .applyDefaultRequestOptions(
                        RequestOptions()
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.drawable.ic_launcher_foreground)
                    )
                    .load(media.getLinkKey().TMDB_IMAGE_PATH())
                    .into(cardImage)
            } else {
                Glide.with(view.context)
                    .applyDefaultRequestOptions(
                        RequestOptions()
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.drawable.ic_launcher_foreground)
                    )
                    .load(media.getLinkKey().YOUTUBE_THUMBNAIL())
                    .into(cardImage)
            }
        }
    }
}