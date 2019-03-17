package com.example.cinemacrazy.media

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cinemacrazy.BaseActivity
import com.example.cinemacrazy.R
import com.example.cinemacrazy.datamodel.BaseMedia
import com.example.cinemacrazy.datamodel.MEDIA_MOVIE
import com.example.cinemacrazy.datamodel.MEDIA_TV
import kotlinx.android.synthetic.main.media_list.*
import org.jetbrains.anko.AnkoLogger

class MediaListActivity : BaseActivity(), AnkoLogger {

    lateinit var adapter: MoviesAdapter
    lateinit var viewmodel: MediaViewModel
    var MEDIA_TYPE = MEDIA_MOVIE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = MoviesAdapter()
        recycler_view.adapter = adapter


        viewmodel = ViewModelProviders.of(this).get(MediaViewModel::class.java)

        displayLiveMedia(MEDIA_TYPE, null)
    }


    private fun displayLiveMedia(mediaType: String, query: String?) {
        viewmodel.setCurrentMediaType(mediaType)
        viewmodel.getCurrentMediaType().observe(this, Observer<String> {

            viewmodel.getMediaLive(api, it, query = query).observe(this,
                Observer<PagedList<BaseMedia>?> { t -> adapter.submitList(t) })
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)

        val searchView = (menu?.findItem(R.id.main_menu_search)?.actionView as SearchView)
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                displayLiveMedia(MEDIA_TYPE, query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        searchView.setOnCloseListener {
            displayLiveMedia(MEDIA_TYPE, null)
            false
        }
        return true
    }

    override fun getLayoutRes(): Int {
        return R.layout.media_list
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

            R.id.main_menu_media_movies -> {
                displayLiveMedia(MEDIA_MOVIE, null)
            }
            R.id.main_menu_media_tv -> {
                displayLiveMedia(MEDIA_TV, null)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}