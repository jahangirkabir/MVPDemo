package com.jahanbabu.mvpdemo.Detail

import com.jahanbabu.mvpdemo.Data.Movie
import com.jahanbabu.mvpdemo.Data.Source.MovieDataSource
import com.jahanbabu.mvpdemo.Data.Source.MovieRepository

/**
 * Listens to user actions from the UI ([DetailFragment]), retrieves the data and updates
 * the UI as required.
 */
class DetailPresenter(private val movieId: String, private val tasksRepository: MovieRepository, private val detailView: DetailContract.View) : DetailContract.Presenter {

    override fun playVideo() {
        detailView.playMovie(myMovie.url)
    }

    override fun pauseVideo() {

    }

    init {
        detailView.presenter = this
    }

    override fun start() {
        getMovieDetails()
    }

    private fun getMovieDetails() {
        detailView.setLoadingIndicator(true)
        tasksRepository.getMovie(movieId, object : MovieDataSource.GetMovieCallback {
            override fun onMovieLoaded(movie: Movie) {
                with(detailView) {
                    // The view may not be able to handle UI updates anymore
                    if (!isActive) {
                        return@onMovieLoaded
                    }
                    setLoadingIndicator(false)
                }
                showMovieData(movie)
            }

            override fun onDataNotAvailable() {
                with(detailView) {
                    // The view may not be able to handle UI updates anymore
                    if (!isActive) {
                        return@onDataNotAvailable
                    }
                }
            }
        })
    }

    private lateinit var myMovie: Movie

    private fun showMovieData(movie: Movie) {
        myMovie = movie
        with(detailView) {
            if (movieId.isEmpty()) {
//                hideTitle()
//                hideDescription()
            } else {
                showTitle(movie.title)
                showDescription(movie.description)
                showThumb(movie.thumb)
                setMovie(movie.url)
            }
        }
    }
}
