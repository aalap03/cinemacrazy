package com.example.cinemacrazy.mainscreen

import com.example.cinemacrazy.datamodel.TrendingMovie

interface IMainActivity {

    fun showMovieList(movieList: ArrayList<TrendingMovie>)

    fun hideLoading()

    fun showLoaging()

    fun showError(errorMsg: String)
}