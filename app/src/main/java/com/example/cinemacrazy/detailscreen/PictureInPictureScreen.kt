package com.example.cinemacrazy.detailscreen

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Rational
import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.net.Uri
import com.example.cinemacrazy.datamodel.utils.YOUTUBE_VIDEO_PATH
import kotlinx.android.synthetic.main.video_view.*
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import androidx.annotation.RequiresApi


class PictureInPictureScreen: AppCompatActivity() {

    private val pictureInPictureParamsBuilder = PictureInPictureParams.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.cinemacrazy.R.layout.video_view)
        pictureInPictureMode()
        val stringExtra = intent.getStringExtra(KEY_VIDEO_URL)
        video_view.setVideoURI(Uri.parse(stringExtra.YOUTUBE_VIDEO_PATH()))

    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun pictureInPictureMode() {
        val aspectRatio = Rational(192, 109)
        pictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build()
        enterPictureInPictureMode(pictureInPictureParamsBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    public override fun onUserLeaveHint() {
        if (!isInPictureInPictureMode) {
            val aspectRatio = Rational(192, 108)
            pictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build()
            enterPictureInPictureMode(pictureInPictureParamsBuilder.build())
        }
    }

}