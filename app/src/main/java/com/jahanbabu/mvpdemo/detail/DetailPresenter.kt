package com.jahanbabu.mvpdemo.detail

import com.jahanbabu.mvpdemo.data.Movie
import com.jahanbabu.mvpdemo.data.source.MovieDataSource
import com.jahanbabu.mvpdemo.data.source.MovieRepository

/**
 * Listens to user actions from the UI ([DetailFragment]), retrieves the data and updates
 * the UI as required.
 */
class DetailPresenter(private var movieId: String, private val tasksRepository: MovieRepository, private val detailView: DetailContract.View) : DetailContract.Presenter {
    override fun setMovieId(movieId: String) {
        this.movieId = movieId
    }

    override fun savePlayBackPosition(playbackPosition: Long) {
        tasksRepository.updateMovie(movieId, playbackPosition)
    }

    override fun requestMoviesFromLocal() {
        tasksRepository.getRelatedMovies(myMovie.title, object : MovieDataSource.LoadMoviesCallback {
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
        detailView.playMovie(myMovie.url, myMovie.position)
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
                setMovie(movie.url, movie.position)
            }
        }
    }

    private fun showRelatedMovieList(movies: List<Movie>) {
        relatedMovies = ArrayList()
        relatedMovies = movies
        with(detailView) {
            setDataToRecyclerView(relatedMovies)
        }
    }
}
