package com.example.cinemacrazy.medialist

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cinemacrazy.BaseActivity
import com.example.cinemacrazy.R
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.BaseMedia
import com.example.cinemacrazy.datamodel.utils.CINEMA_TYPE_MOVIE
import com.example.cinemacrazy.datamodel.utils.CINEMA_TYPE_TV
import kotlinx.android.synthetic.main.media_list.*
import org.jetbrains.anko.AnkoLogger

class CinemaListActivity : BaseActivity(), AnkoLogger {

    lateinit var adapter: MoviesAdapter
    lateinit var viewmodel: CinemaViewModel
    var MEDIA_TYPE = CINEMA_TYPE_MOVIE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = ""

        setRecyclerView()
        setSpinner()

        viewmodel = ViewModelProviders.of(this).get(CinemaViewModel::class.java)
        displayLiveMedia(MEDIA_TYPE, null)
    }

    fun setRecyclerView() {
        recycler_view.layoutManager = GridLayoutManager(this, 2)
        adapter = MoviesAdapter()
        recycler_view.adapter = adapter
    }

    private fun setSpinner() {
        media_selector.setItems("Trending Movies", "Trending TV Shows")
        media_selector.setOnItemSelectedListener { _, position, _, _ ->
            displayLiveMedia(if (position == 0) CINEMA_TYPE_MOVIE else CINEMA_TYPE_TV, null)
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.media_list
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

    private fun displayLiveMedia(mediaType: String, query: String?) {
        viewmodel.setCurrentMediaType(mediaType)
        viewmodel.getCurrentMediaType().observe(this, Observer<String> {
            viewmodel.getMediaLive(api, it, query = query).observe(this,
                Observer<PagedList<BaseMedia>?> { t -> adapter.submitList(t) })
        })
    }
}