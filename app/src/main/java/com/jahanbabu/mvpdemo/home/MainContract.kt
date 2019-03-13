package com.jahanbabu.mvpdemo.home

import com.jahanbabu.mvpdemo.BasePresenter
import com.jahanbabu.mvpdemo.BaseView
import com.jahanbabu.mvpdemo.data.Movie

interface MainContract {

    interface View : BaseView<Presenter> {

        fun showProgress()

        fun hideProgress()

        fun setDataToRecyclerView(movieArrayList: ArrayList<Movie>)

        fun onResponseFailure(throwable: Throwable)

        fun navigateToDetailScreen(complete: Boolean)
    }

    interface Presenter : BasePresenter {

        fun onDestroy()

        fun onRefreshButtonClick()

        fun requestDataFromServer()

        fun saveMovies(list: ArrayList<Movie>)

    }

    /**
     * Intractors are classes built for fetching data from your database, web services, or any other data source.
     **/
    interface GetMovieIntractor {

        interface OnFinishedListener {
            fun onFinished(movieArrayList: ArrayList<Movie>)
            fun onFailure(t: Throwable)
        }

        fun getMovieArrayList(onFinishedListener: OnFinishedListener)
    }
}
