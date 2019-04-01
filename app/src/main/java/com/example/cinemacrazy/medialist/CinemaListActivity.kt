package com.example.cinemacrazy.medialist

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import co.zsmb.materialdrawerkt.builders.DrawerBuilderKt
import com.example.cinemacrazy.BaseActivity
import com.example.cinemacrazy.R
import com.example.cinemacrazy.datamodel.serverResponses.cinemaResponses.BaseMedia
import com.example.cinemacrazy.datamodel.utils.constant.*
import com.example.cinemacrazy.datamodel.utils.constant.ApiConstants.Companion.API_MOVIE_CINEMALIST_NOW_PLAYING
import com.example.cinemacrazy.datamodel.utils.constant.ApiConstants.Companion.API_CINEMALIST_POPULAR
import com.example.cinemacrazy.datamodel.utils.constant.ApiConstants.Companion.API_CINEMALIST_TOP_RATED
import com.example.cinemacrazy.datamodel.utils.constant.ApiConstants.Companion.API_CINEMALIST_TRENDING
import com.example.cinemacrazy.datamodel.utils.constant.ApiConstants.Companion.API_MOVIE_CINEMALIST_UPCOMING
import com.example.cinemacrazy.datamodel.utils.constant.ApiConstants.Companion.API_TV_CINEMALIST_AIRING_TODAY
import com.example.cinemacrazy.datamodel.utils.constant.ApiConstants.Companion.API_TV_CINEMALIST_LATEST
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
    private var CINEMA_TYPE = CINEMA_TYPE_MOVIE
    private var CINEMA_LIST_TYPE = API_CINEMALIST_TRENDING
    lateinit var drawer: Drawer
    var currentSelectedIdentifier = 1L

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
        displayLiveMedia(null)
        viewmodel.initialLoading().observe(this, Observer { progress.visibility = if (it) View.VISIBLE else View.GONE })

        supportActionBar?.title = getToolbarTitle()
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
        searchView.setOnSearchClickListener { searchView.queryHint = "Search ${CINEMA_TYPE.toUpperCase()}" }
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                displayLiveMedia(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        searchView.setOnCloseListener {
            displayLiveMedia(null)
            false
        }
        return true
    }

    private fun displayLiveMedia(query: String?) {
        info { "displayLiveMedia: $CINEMA_TYPE->$CINEMA_LIST_TYPE" }
        viewmodel.getMediaLive(api, CINEMA_TYPE, query = query, cinemaListType = CINEMA_LIST_TYPE).observe(this,
            Observer<PagedList<BaseMedia>?> { t -> adapter.submitList(t) })

    }

    private fun addDrawerItems() {
        drawer.addItem(PrimaryDrawerItem().apply {
            withName("Cinem@ Cr@zy")
            withSelectable(false)
            withSubItems()
        })
        drawer.addItem(ExpandableDrawerItem().apply {
            withName("Movies")
            withSubItems(
                getSecondaryItemWithName(
                    getString(R.string.movie_trending),
                    IDENTIFIER_TRENDING_MOVIE
                ),
                getSecondaryItemWithName(
                    getString(R.string.movie_now_playing),
                    IDENTIFIER_NOW_PLAYING_MOVIE
                ),
                getSecondaryItemWithName(
                    getString(R.string.movie_popular),
                    IDENTIFIER_POPULAR_MOVIE
                ),
                getSecondaryItemWithName(
                    getString(R.string.movie_top_rated),
                    IDENTIFIER_TOP_RATED_MOVIE
                ),
                getSecondaryItemWithName(
                    getString(R.string.movie_upcoming),
                    IDENTIFIER_UPCOMING_MOVIE
                )
            )
        })
        drawer.addItem(ExpandableDrawerItem().apply {
            withName("TV")
            withSubItems(
                getSecondaryItemWithName(
                    getString(R.string.tv_trending),
                    IDENTIFIER_TRENDING_TV
                ),
                getSecondaryItemWithName(
                    getString(R.string.tv_latest),
                    IDENTIFIER_LATEST_TV
                ),
                getSecondaryItemWithName(
                    getString(R.string.tv_popular),
                    IDENTIFIER_POPULAR_TV
                ),
                getSecondaryItemWithName(
                    getString(R.string.tv_top_rated),
                    IDENTIFIER_TOP_RATED_TV
                ),
                getSecondaryItemWithName(
                    getString(R.string.tv_airing_today),
                    IDENTIFIER_AIRING_TODAY_TV
                )
            )
        })
    }

    private fun getSecondaryItemWithName(name: String, identifier: Long): SecondaryDrawerItem {
        return SecondaryDrawerItem().apply {
            withName(name)
            withSetSelected(true)
            withIdentifier(identifier)
            withOnDrawerItemClickListener { _, _, drawerItem ->

                if (currentSelectedIdentifier != drawerItem.identifier) {
                    CINEMA_LIST_TYPE = getCinemaListTypeFromDrawableOnClick(drawerItem.identifier)
                    CINEMA_TYPE = getCinemaTypeFromDrawableOnClick(drawerItem.identifier)
                    cinema_list_toolbar.title = getToolbarTitle()
                    displayLiveMedia(null)
                    currentSelectedIdentifier = drawerItem.identifier
                }
                false
            }
        }
    }

    private fun getToolbarTitle(): CharSequence? {
        val cinemaListTye = when (CINEMA_LIST_TYPE) {
            API_CINEMALIST_POPULAR -> getString(R.string.movie_popular)
            API_CINEMALIST_TRENDING -> getString(R.string.movie_trending)
            API_CINEMALIST_TOP_RATED -> getString(R.string.movie_top_rated)
            API_MOVIE_CINEMALIST_NOW_PLAYING -> getString(R.string.movie_now_playing)
            API_MOVIE_CINEMALIST_UPCOMING -> getString(R.string.movie_upcoming)
            API_TV_CINEMALIST_AIRING_TODAY -> getString(R.string.tv_airing_today)
            API_TV_CINEMALIST_LATEST -> getString(R.string.tv_latest)
            else -> ""
        }
        return "$cinemaListTye (${CINEMA_TYPE.toUpperCase()})"
    }

    private fun getCinemaTypeFromDrawableOnClick(fromIdentifier: Long): String {
        return if (fromIdentifier > 5L) {
            CINEMA_TYPE_TV
        } else {
            CINEMA_TYPE_MOVIE
        }
    }

    private fun getCinemaListTypeFromDrawableOnClick(fromIdentifier: Long): String {
        return when (fromIdentifier) {
            IDENTIFIER_TRENDING_MOVIE -> API_CINEMALIST_TRENDING
            IDENTIFIER_NOW_PLAYING_MOVIE -> API_MOVIE_CINEMALIST_NOW_PLAYING
            IDENTIFIER_POPULAR_MOVIE -> API_CINEMALIST_POPULAR
            IDENTIFIER_TOP_RATED_MOVIE -> API_CINEMALIST_TOP_RATED
            IDENTIFIER_UPCOMING_MOVIE -> API_MOVIE_CINEMALIST_UPCOMING

            IDENTIFIER_TRENDING_TV -> API_CINEMALIST_TRENDING
            IDENTIFIER_LATEST_TV -> API_TV_CINEMALIST_LATEST
            IDENTIFIER_POPULAR_TV -> API_CINEMALIST_POPULAR
            IDENTIFIER_TOP_RATED_TV -> API_CINEMALIST_TOP_RATED
            IDENTIFIER_AIRING_TODAY_TV -> API_TV_CINEMALIST_AIRING_TODAY

            else -> "Invalid"
        }
    }
}