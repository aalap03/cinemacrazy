package com.example.cinemacrazy.medialist

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cinemacrazy.BaseActivity
import com.example.cinemacrazy.R
import com.example.cinemacrazy.application.getColor
import com.example.cinemacrazy.datamodel.BaseMedia
import com.example.cinemacrazy.datamodel.CINEMA_TYPE_MOVIE
import com.example.cinemacrazy.datamodel.CINEMA_TYPE_TV
import kotlinx.android.synthetic.main.media_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class CinemaListActivity : BaseActivity(), AnkoLogger {

    lateinit var adapter: MoviesAdapter
    lateinit var viewmodel: CinemaViewModel
    var MEDIA_TYPE = CINEMA_TYPE_MOVIE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = MoviesAdapter()
        supportActionBar?.setTitle("")
        recycler_view.adapter = adapter
        media_selector.setArrowColor(R.color.colorAccent.getColor(this))
        media_selector.setTextColor(R.color.colorPrimaryDark.getColor(this))
        media_selector.setBackgroundColor(R.color.colorPrimary.getColor(this))
        media_selector.setItems("Trending Movies", "Trending TV Shows")
        media_selector.setOnItemSelectedListener { _, position, _, _ ->
            displayLiveMedia(if (position == 0) CINEMA_TYPE_MOVIE else CINEMA_TYPE_TV, null)
        }

        viewmodel = ViewModelProviders.of(this).get(CinemaViewModel::class.java)


        info { "Info: ${getList()}" }


        displayLiveMedia(MEDIA_TYPE, null)
    }

    override fun getLayoutRes(): Int {
        return R.layout.media_list
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

    fun test(): String {
        var list = arrayListOf<Test>()
        list.add(Test(1, "Aalap1", 1))
        list.add(Test(2, "Aalap2", 2))
        list.add(Test(3, "Aalap3", 3))
        return list.joinToString(separator = ":")
    }

    private fun getList(): Int {
        val test = test()
        val split = test.split(":")
        split.forEach {
            val split1 = it.split(",")
            info { "Info: inside length ${split.size}" }
            split1.forEach {
                info { "Info: inside $it" }
            }
        }
        return split.size
    }

    data class Test(
        var id: Int, var genre: String, var int: Long
    )
}