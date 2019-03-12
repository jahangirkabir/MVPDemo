package com.jahanbabu.mvpdemo.Home

import android.content.Context
import com.jahanbabu.mvpdemo.Data.Movie
import com.jahanbabu.mvpdemo.Data.Source.MovieDataSource
import com.jahanbabu.mvpdemo.Data.Source.MovieRepository

/**
 * Listens to user actions from the UI ([MainFragment]), retrieves the data and updates
 * the UI as required.
 */
class MainPresenter(
    val mainView: MainContract.View,
    val getMovieIntractor: MainContract.GetMovieIntractor,
    val tasksRepository: MovieDataSource
) : MainContract.Presenter, MainContract.GetMovieIntractor.OnFinishedListener {
    override fun saveMovies(list: ArrayList<Movie>) {
        for (m in list){
            tasksRepository.saveMovie(m)
        }
    }

    override fun onDestroy() {

    }

    override fun onRefreshButtonClick() {

    }

    override fun requestDataFromServer() {
        getMovieIntractor.getMovieArrayList(this)
    }

    override fun onFinished(movieArrayList: ArrayList<Movie>) {
        if (mainView != null) {
            mainView.setDataToRecyclerView(movieArrayList)
            mainView.hideProgress()
        }
    }

    override fun onFailure(t: Throwable) {
        if (mainView != null) {
            mainView.onResponseFailure(t)
            mainView.hideProgress()
        }
    }


    init {
        mainView.presenter = this
    }

    lateinit var mContext: Context

    override fun start() {
        mainView.presenter = this
    }

}
