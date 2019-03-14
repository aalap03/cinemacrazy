package com.example.cinemacrazy.mainscreen

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainPresenter @Inject constructor(var view: IMainActivity, var movieRepository: Repository) {

    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun requestMovies() {
        view.showLoaging()

        compositeDisposable.add(
            movieRepository.getTrendingMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    t?.let { view.showMovieList(it) }
                    view.hideLoading()
                }, { t ->
                    view.showError(t.localizedMessage)
                    view.hideLoading()
                })
        )

    }

    fun clear() {
        compositeDisposable.clear()
    }
}