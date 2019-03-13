package com.jahanbabu.mvpdemo.detail

import com.jahanbabu.mvpdemo.Data.Movie
import com.jahanbabu.mvpdemo.Data.Source.MovieDataSource
import com.jahanbabu.mvpdemo.Data.Source.MovieRepository

/**
 * Listens to user actions from the UI ([DetailFragment]), retrieves the data and updates
 * the UI as required.
 */
class DetailPresenter(private val movieId: String, private val tasksRepository: MovieRepository, private val detailView: DetailContract.View) : DetailContract.Presenter {

    override fun requestMoviesFromLocal() {
        tasksRepository.getMovies(object : MovieDataSource.LoadMoviesCallback {
            override fun onMovieLoaded(movies: List<Movie>) {
                with(detailView) {
                    // The view may not be able to handle UI updates anymore
                    if (!isActive) {
                        return@onMovieLoaded
                    }
                    setLoadingIndicator(false)
                }
                showRelatedMovieList(movies)
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
                requestMoviesFromLocal()
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
    private lateinit var relatedMovies: List<Movie>

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

    private fun showRelatedMovieList(movies: List<Movie>) {
        relatedMovies = movies
        with(detailView) {
            setDataToRecyclerView(relatedMovies)
        }
    }
}
