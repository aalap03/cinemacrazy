package com.example.cinemacrazy.detailscreen

import android.os.Bundle
import android.util.Rational
import android.app.PictureInPictureParams
import android.os.Build
import android.util.DisplayMetrics
import kotlinx.android.synthetic.main.video_view.*
import com.example.cinemacrazy.datamodel.utils.constant.ApiConstants
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class PictureInPictureScreen : YouTubeBaseActivity(), AnkoLogger {

    private val pictureInPictureParamsBuilder = PictureInPictureParams.Builder()
    lateinit var onInitializeListenr: YouTubePlayer.OnInitializedListener
    var width = 0
    var height = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.cinemacrazy.R.layout.video_view)
        val videoLinks = intent.getStringArrayListExtra(KEY_VIDEO_LINKS)

        val displayMetrics = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels * 3 / 4
        height = displayMetrics.heightPixels * 1 / 4

        pictureInPictureMode()

        onInitializeListenr = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                player?.loadVideos(videoLinks)
            }

            override fun onInitializationFailure(
                provider: YouTubePlayer.Provider?,
                result: YouTubeInitializationResult?
            ) {
            }

        }
        youtube_player_view.initialize(ApiConstants.YOUTUBE_API_KEY, onInitializeListenr)
    }

    private fun pictureInPictureMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val aspectRatio = Rational(width, height)
            pictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build()
            enterPictureInPictureMode(pictureInPictureParamsBuilder.build())
        }
    }

    public override fun onUserLeaveHint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!isInPictureInPictureMode) {
                val aspectRatio = Rational(width, height)
                pictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build()
                enterPictureInPictureMode(pictureInPictureParamsBuilder.build())
            }
        }
    }

}