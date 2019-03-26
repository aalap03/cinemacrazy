package com.example.cinemacrazy.medialist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import co.zsmb.materialdrawerkt.builders.DrawerBuilderKt
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import com.example.cinemacrazy.BaseActivity
import com.example.cinemacrazy.R
import com.example.cinemacrazy.application.App
import com.example.cinemacrazy.application.AppDb
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.BaseMedia
import com.example.cinemacrazy.datamodel.utils.CINEMA_TYPE_MOVIE
import com.example.cinemacrazy.datamodel.utils.CINEMA_TYPE_TV
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import kotlinx.android.synthetic.main.media_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import kotlin.concurrent.thread

class CinemaListActivity : BaseActivity(), AnkoLogger {

    lateinit var adapter: MoviesAdapter
    lateinit var viewmodel: CinemaViewModel
    var CINEMA_TYPE = CINEMA_TYPE_MOVIE
    var CINEMA_LIST_TYPE = ""
    lateinit var drawer: Drawer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(cinema_list_toolbar)
        drawer = DrawerBuilderKt(this)
            .builder
            .withToolbar(cinema_list_toolbar)
            .build()
        setupDrawer()

        supportActionBar?.title = ""

        setRecyclerView()
        //setSpinner()

        viewmodel = ViewModelProviders.of(this).get(CinemaViewModel::class.java)
        displayLiveMedia(CINEMA_TYPE, null, "")

    }

    private fun setupDrawer() {

        drawer.addItems(
            PrimaryDrawerItem().withName("Aalap"),
            SecondaryDrawerItem().withName("Ankita").withOnDrawerItemClickListener(object: Drawer.OnDrawerItemClickListener {
                override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*, *>?): Boolean {

                    return true
                }
            }),
            DividerDrawerItem(),
            SecondaryDrawerItem().withName("Ankita")
        )
    }

    private fun setRecyclerView() {
        recycler_view.layoutManager = GridLayoutManager(this, 2)
        adapter = MoviesAdapter()
        recycler_view.adapter = adapter
    }

//    private fun setSpinner() {
//        media_selector.setItems("Trending Movies", "Trending TV Shows")
//        media_selector.setOnItemSelectedListener { _, position, _, _ ->
//            displayLiveMedia(if (position == 0) CINEMA_TYPE_MOVIE else CINEMA_TYPE_TV, null, CINEMA_LIST_TYPE)
//        }
//    }

    override fun getLayoutRes(): Int {
        return R.layout.media_list
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)

        val searchView = (menu?.findItem(R.id.main_menu_search)?.actionView as SearchView)
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                displayLiveMedia(CINEMA_TYPE, query, CINEMA_LIST_TYPE)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        searchView.setOnCloseListener {
            displayLiveMedia(CINEMA_TYPE, null, CINEMA_LIST_TYPE)
            false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.main_menu_now_playing -> displayLiveMedia(CINEMA_TYPE, null, NOW_PLAYING)
            R.id.main_menu_upcoming -> displayLiveMedia(CINEMA_TYPE, null, UPCOMING)
            R.id.main_menu_popular -> displayLiveMedia(CINEMA_TYPE, null, POPULAR)
            R.id.main_menu_top_rated -> displayLiveMedia(CINEMA_TYPE, null, TOP_RATED)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayLiveMedia(mediaType: String, query: String?, cinemaListType: String) {
        viewmodel.setCurrentMediaType(mediaType)
        viewmodel.getCurrentMediaType().observe(this, Observer<String> {
            viewmodel.getMediaLive(api, it, query = query, cinemaListType = cinemaListType).observe(this,
                Observer<PagedList<BaseMedia>?> { t -> adapter.submitList(t) })
        })
    }
}