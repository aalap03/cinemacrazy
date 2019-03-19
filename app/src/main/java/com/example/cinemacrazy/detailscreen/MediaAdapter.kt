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
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Context
import android.net.Uri


class MediaAdapter : ListAdapter<MovieMedia, MediaAdapter.MediaHolder>(object : DiffUtil.ItemCallback<MovieMedia>() {

    override fun areItemsTheSame(oldItem: MovieMedia, newItem: MovieMedia): Boolean {
        return oldItem.getLinkKey() == newItem.getLinkKey()
    }

    override fun areContentsTheSame(oldItem: MovieMedia, newItem: MovieMedia): Boolean {
        return oldItem.getLinkKey() == newItem.getLinkKey()
    }

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaHolder{

        return MediaHolder(LayoutInflater.from(parent.context).inflate(R.layout.simple_image_card_item, parent, false))
    }


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
                            .placeholder(R.drawable.gradient.getDrawable(context = itemView.context))
                            .error(R.drawable.gradient.getDrawable(context = itemView.context))
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

                cardImage.setOnClickListener { v -> watchYoutubeVideo(v.context, media.getLinkKey()) }
            }
        }
    }

    fun watchYoutubeVideo(context: Context, id: String) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$id")
        )
        try {
            context.startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(webIntent)
        }

    }
}