package com.example.cinemacrazy.mainscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cinemacrazy.R
import com.example.cinemacrazy.apiservice.TmdbService
import com.example.cinemacrazy.datamodel.TrendingMovie
import com.example.cinemacrazy.di.components.DaggerMoviesComponent
import com.example.cinemacrazy.di.modules.ContextModule
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import javax.inject.Inject

class MainActivity : AppCompatActivity(), AnkoLogger, IMainActivity {

    lateinit var presenter: MainPresenter
    @Inject
    lateinit var api: TmdbService

    lateinit var adapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = MoviesAdapter()
        recycler_view.adapter = adapter

        val moviesComponent = DaggerMoviesComponent
            .builder()
            .contextModule(ContextModule(this))
            .build()
        moviesComponent.inject(this)

        presenter = MainPresenter(this, MoviesModel(api))
        presenter.requestMovies()

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.clear()
    }

    override fun showMovieList(movieList: ArrayList<TrendingMovie>) {
        Toast.makeText(this, "Success: ${movieList.size}", Toast.LENGTH_SHORT)
            .show()
        adapter.submitList(movieList)
    }

    override fun hideLoading() {
        progress.visibility = View.GONE
    }

    override fun showLoaging() {
        progress.visibility = View.VISIBLE
    }

    override fun showError(errorMsg: String) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
    }
}
