package com.jahanbabu.mvpdemo.Detail

import com.jahanbabu.mvpdemo.BasePresenter
import com.jahanbabu.mvpdemo.BaseView


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

        fun setMovie(url: String)

        fun playMovie(url: String)
    }

    interface Presenter : BasePresenter {

        fun playVideo()

        fun pauseVideo()

    }
}
