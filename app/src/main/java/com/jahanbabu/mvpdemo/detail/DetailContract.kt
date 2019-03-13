package com.jahanbabu.mvpdemo.detail

import com.jahanbabu.mvpdemo.BasePresenter
import com.jahanbabu.mvpdemo.BaseView
import com.jahanbabu.mvpdemo.data.Movie


/**
 * This specifies the contract between the view and the presenter.
 */
interface DetailContract {

    interface View : BaseView<Presenter> {

        val isActive: Boolean

        fun setLoadingIndicator(active: Boolean)

        fun showTitle(title: String)

        fun showDescription(description: String)

        fun showThumb(thumb: String)

        fun setDataToRecyclerView(movieArrayList: List<Movie>)

        fun setMovie(url: String, position: Long)

        fun playMovie(url: String, position: Long)
    }

    interface Presenter : BasePresenter {

        fun setMovieId(movieId: String)

        fun requestMoviesFromLocal()

        fun playVideo()

        fun savePlayBackPosition(playbackPosition: Long)

        fun pauseVideo()

    }
}
