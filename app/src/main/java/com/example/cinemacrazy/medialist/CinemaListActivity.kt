package com.example.cinemacrazy.medialist

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import co.zsmb.materialdrawerkt.builders.DrawerBuilderKt
import com.example.cinemacrazy.BaseActivity
import com.example.cinemacrazy.R
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.BaseMedia
import com.example.cinemacrazy.datamodel.utils.*
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import kotlinx.android.synthetic.main.media_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

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
            .withDisplayBelowStatusBar(true)
            .withActionBarDrawerToggleAnimated(true)
            .build()
        addDrawerItems()

        setRecyclerView()

        viewmodel = ViewModelProviders.of(this).get(CinemaViewModel::class.java)
        displayLiveMedia(CINEMA_TYPE, null, "")

    }

    private fun setRecyclerView() {
        recycler_view.layoutManager = GridLayoutManager(this, 2)
        adapter = MoviesAdapter()
        recycler_view.adapter = adapter
    }

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

    private fun displayLiveMedia(mediaType: String, query: String?, cinemaListType: String) {
        viewmodel.setCurrentMediaType(mediaType)
        viewmodel.getCurrentMediaType().observe(this, Observer<String> {
            viewmodel.getMediaLive(api, it, query = query, cinemaListType = cinemaListType).observe(this,
                Observer<PagedList<BaseMedia>?> { t -> adapter.submitList(t) })
        })
    }

    fun showBackArrow() {
        drawer.actionBarDrawerToggle.isDrawerIndicatorEnabled = false
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun showHamburgerIcon() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        drawer.actionBarDrawerToggle.isDrawerIndicatorEnabled = true
    }

    private fun addDrawerItems() {
        drawer.addItem(PrimaryDrawerItem().apply {
            withName("Cinem@Cr@zy")
            withSelectable(false)
            withSubItems()
        })

        drawer.addItem(ExpandableDrawerItem().apply {
            withName("Movies")
            withSubItems(
                getSecondaryItemWithName(getString(R.string.movie_trending), TRENDING_MOVIE),
                getSecondaryItemWithName(getString(R.string.movie_now_playing), NOW_PLAYING_MOVIE),
                getSecondaryItemWithName(getString(R.string.movie_popular), POPULAR_MOVIE),
                getSecondaryItemWithName(getString(R.string.movie_top_rated), TOP_RATED_MOVIE),
                getSecondaryItemWithName(getString(R.string.movie_upcoming), UPCOMING_MOVIE)
            )
        })

        drawer.addItem(ExpandableDrawerItem().apply {
            withName("TV")
            withSubItems(
                getSecondaryItemWithName(getString(R.string.tv_trending), TRENDING_TV),
                getSecondaryItemWithName(getString(R.string.tv_latest), LATEST_TV),
                getSecondaryItemWithName(getString(R.string.tv_popular), POPULAR_TV),
                getSecondaryItemWithName(getString(R.string.tv_top_rated), TOP_RATED_TV),
                getSecondaryItemWithName(getString(R.string.tv_airing_today), AIRING_TODAY_TV)
            )
        })
    }

    private fun getSecondaryItemWithName(name: String, identifier: Long): SecondaryDrawerItem {
        return SecondaryDrawerItem().apply {
            withName(name)
            withIdentifier(identifier)
            withOnDrawerItemClickListener { view, position, drawerItem ->
                info { "Item: identifier ${drawerItem.identifier}" }
                info { "Item: position $position" }
             false
            }
        }
    }
}