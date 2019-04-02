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
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Context
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.cinemacrazy.datamodel.serverResponses.mediaResponses.IMAGE
import com.example.cinemacrazy.datamodel.serverResponses.mediaResponses.MovieMedia
import com.example.cinemacrazy.datamodel.utils.TMDB_BACKDROP_IMAGE_PATH
import com.example.cinemacrazy.datamodel.utils.YOUTUBE_THUMBNAIL
import com.example.cinemacrazy.datamodel.utils.YOUTUBE_VIDEO_PATH
import com.example.cinemacrazy.datamodel.utils.getDrawable

val KEY_VIDEO_URL = "key_video_url"

class MediaAdapter(var callback: ImageClickCallback?) :
    ListAdapter<MovieMedia, MediaAdapter.MediaHolder>(object : DiffUtil.ItemCallback<MovieMedia>() {

        override fun areItemsTheSame(oldItem: MovieMedia, newItem: MovieMedia): Boolean {
            return oldItem.getLinkKey() == newItem.getLinkKey()
        }

        override fun areContentsTheSame(oldItem: MovieMedia, newItem: MovieMedia): Boolean {
            return oldItem.getLinkKey() == newItem.getLinkKey()
        }

    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaHolder {

        return MediaHolder(LayoutInflater.from(parent.context).inflate(R.layout.simple_image_card_item, parent, false))
    }


    override fun onBindViewHolder(holder: MediaHolder, position: Int) {
        holder.bindView(position, getItem(position))
    }

    inner class MediaHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var cardImage: ImageView = view.findViewById(R.id.simple_card_image)

        fun bindView(position: Int, media: MovieMedia) {

            if (media.mediaType() == IMAGE) {
                Glide.with(view.context)
                    .applyDefaultRequestOptions(
                        RequestOptions()
                            .placeholder(R.drawable.gradient.getDrawable(context = itemView.context))
                            .error(R.drawable.gradient.getDrawable(context = itemView.context))
                    )
                    .load(media.getLinkKey().TMDB_BACKDROP_IMAGE_PATH())
                    .into(cardImage)
                cardImage.setOnClickListener { callback?.imageClicked(position, media.getLinkKey()) }

            } else {
                Glide.with(view.context)
                    .applyDefaultRequestOptions(
                        RequestOptions()
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.drawable.ic_launcher_background)
                    )
                    .load(media.getLinkKey().YOUTUBE_THUMBNAIL())
                    .into(cardImage)

                cardImage.setOnClickListener { v ->
                    watchYoutubeVideo(v.context, media.getLinkKey())
                    //openPiP(media.getLinkKey())
                }
            }
        }

        private fun openPiP(linkKey: String) {
            var intent = Intent(view.context, PictureInPictureScreen::class.java)
            intent.putExtra(KEY_VIDEO_URL, linkKey)
            view.context.startActivity(intent)
        }
    }

    fun watchYoutubeVideo(context: Context, id: String) {

        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$id"))
        try {
            context.startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(webIntent)
        }
    }
}

interface ImageClickCallback {
    fun imageClicked(position: Int, imageKey: String)
}