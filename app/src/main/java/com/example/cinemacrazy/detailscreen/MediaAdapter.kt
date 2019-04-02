package com.example.cinemacrazy.detailscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinemacrazy.datamodel.serverResponses.mediaResponses.IMAGE
import com.example.cinemacrazy.datamodel.serverResponses.mediaResponses.MovieMedia
import com.example.cinemacrazy.datamodel.utils.TMDB_BACKDROP_IMAGE_PATH
import com.example.cinemacrazy.R
import com.example.cinemacrazy.datamodel.utils.YOUTUBE_THUMBNAIL
import com.example.cinemacrazy.datamodel.utils.loadImage

const val KEY_VIDEO_LINKS = "key_video_links"

class MediaAdapter(var callback: MediaClickCallback) :
    ListAdapter<MovieMedia, MediaAdapter.MediaHolder>(object : DiffUtil.ItemCallback<MovieMedia>() {
        override fun areItemsTheSame(oldItem: MovieMedia, newItem: MovieMedia) =
            oldItem.getLinkKey() == newItem.getLinkKey()

        override fun areContentsTheSame(oldItem: MovieMedia, newItem: MovieMedia) =
            oldItem.getLinkKey() == newItem.getLinkKey()
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MediaHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.simple_image_card_item, parent, false
        )
    )

    override fun onBindViewHolder(holder: MediaHolder, position: Int) = holder.bindView(position, getItem(position))

    inner class MediaHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var cardImage: ImageView = view.findViewById(R.id.simple_card_image)

        fun bindView(position: Int, media: MovieMedia) {

            if (media.mediaType() == IMAGE) {

                Glide
                    .with(view.context)
                    .loadImage(media.getLinkKey().TMDB_BACKDROP_IMAGE_PATH(), cardImage)

                cardImage.setOnClickListener {
                    callback.mediaClicked(
                        position = position,
                        mediaKey = media.getLinkKey(),
                        isMediaImage = true
                    )
                }
            } else {
                Glide
                    .with(view.context)
                    .loadImage(media.getLinkKey().YOUTUBE_THUMBNAIL(), cardImage)

                cardImage.setOnClickListener {
                    callback.mediaClicked(
                        position = position,
                        mediaKey = media.getLinkKey(),
                        isMediaImage = false
                    )
                }
            }
        }
    }
}

interface MediaClickCallback {
    fun mediaClicked(position: Int, mediaKey: String, isMediaImage: Boolean)
}