package com.example.cinemacrazy.detailscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.cinemacrazy.R
import kotlinx.android.synthetic.main.cinema_image_screen.*

class ImageScreen : AppCompatActivity() {

    var totalSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cinema_image_screen)

        setSupportActionBar(cinema_image_screen_toolbar)

        val selectedImagePosition = intent.getIntExtra(KEY_POSITION, 0)
        val imagesList = intent.getStringArrayListExtra(KEY_IMAGE_LIST)
        totalSize = imagesList.size

        setToolbarTitle(selectedImagePosition)

        val imagesPagerAdapter = ImagesPagerAdapter(imagesList)
        cinema_image_screen_viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        cinema_image_screen_viewpager.adapter = imagesPagerAdapter
        cinema_image_screen_viewpager.setCurrentItem(selectedImagePosition, true)

        cinema_image_screen_viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                setToolbarTitle(position)
            }
        })
    }

    private fun setToolbarTitle(position: Int) {
        cinema_image_screen_toolbar.title = "Images (${position + 1}/$totalSize)"
    }
}