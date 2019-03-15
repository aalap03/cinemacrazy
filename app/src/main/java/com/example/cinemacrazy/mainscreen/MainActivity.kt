package com.example.cinemacrazy.mainscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cinemacrazy.BaseActivity
import com.example.cinemacrazy.R
import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.datamodel.TrendingMovie
import com.example.cinemacrazy.di.components.DaggerMoviesComponent
import com.example.cinemacrazy.di.modules.ContextModule
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import javax.inject.Inject

class MainActivity : BaseActivity(), AnkoLogger {
    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    lateinit var presenter: MainPresenter

    lateinit var adapter: MoviesAdapter
    lateinit var viewmodel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = MoviesAdapter()
        recycler_view.adapter = adapter



//        presenter = MainPresenter(this, MoviesModel(api))
//        presenter.requestMovies(1)

        viewmodel = ViewModelProviders.of(this).get(MoviesViewModel::class.java)
        viewmodel.getLiveMovies(api)
            .observe(this,
                Observer<PagedList<TrendingMovie>?> { t -> adapter.submitList(t) })


    }

//    override fun showMovieList(movieList: ArrayList<TrendingMovie>) {
//        Toast.makeText(this, "Success: ${movieList.size}", Toast.LENGTH_SHORT)
//            .show()
//        adapter.submitList(movieList)
//    }
//
//    override fun hideLoading() {
//        progress.visibility = View.GONE
//    }
//
//    override fun showLoaging() {
//        progress.visibility = View.VISIBLE
//    }
//
//    override fun showError(errorMsg: String) {
//        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
//    }
}
