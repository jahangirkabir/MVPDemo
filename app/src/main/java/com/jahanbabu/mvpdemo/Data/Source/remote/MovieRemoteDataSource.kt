package com.jahanbabu.mvpdemo.Data.Source.remote

import android.os.Handler
import androidx.annotation.VisibleForTesting
import com.jahanbabu.mvpdemo.Data.Source.MovieDataSource
import com.google.common.collect.Lists
import com.jahanbabu.mvpdemo.Data.Movie
import com.jahanbabu.mvpdemo.Data.Source.local.MoviesDao
import com.jahanbabu.mvpdemo.util.AppExecutors

/**
 * Implementation of the data source that adds a latency simulating network.
 */
class MovieRemoteDataSource private constructor(
    val appExecutors: AppExecutors,
    val moviesDao: MoviesDao
) : MovieDataSource {

    private val SERVICE_LATENCY_IN_MILLIS = 5000L

    private var MOVIES_SERVICE_DATA = LinkedHashMap<String, Movie>()

    private fun addMovie(id: String, title: String, description: String, thumb: String, url: String) {
        val movie = Movie(id, title, description, thumb, url)
        MOVIES_SERVICE_DATA.put(movie.id, movie)
    }

    override fun saveMovie(movie: Movie) {
        MOVIES_SERVICE_DATA.put(movie.id, movie)
    }

    /**
     * Note: [MovieDataSource.LoadMoviesCallback.onDataNotAvailable] is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    override fun getMovies(callback: MovieDataSource.LoadMoviesCallback) {
        // Simulate network by delaying the execution.
        val movies = Lists.newArrayList(MOVIES_SERVICE_DATA.values)
        Handler().postDelayed({
            callback.onMovieLoaded(movies)
        }, SERVICE_LATENCY_IN_MILLIS)
    }

    /**
     * Note: [MovieDataSource.GetMovieCallback.onDataNotAvailable] is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    override fun getMovie(movieId: String, callback: MovieDataSource.GetMovieCallback) {
        val task = MOVIES_SERVICE_DATA[movieId]

        // Simulate network by delaying the execution.
        with(Handler()) {
            if (task != null) {
                postDelayed({ callback.onMovieLoaded(task) }, SERVICE_LATENCY_IN_MILLIS)
            } else {
                postDelayed({ callback.onDataNotAvailable() }, SERVICE_LATENCY_IN_MILLIS)
            }
        }
    }

    override fun updateMovie(movieId: String) {
        // Not required for the remote data source because the {@link MovieRepository} handles
        // converting from a {@code movieId} to a {@link movie} using its cached data.
    }



    override fun refreshMovies() {
        // Not required because the {@link MovieRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    override fun deleteAllMovies() {
        MOVIES_SERVICE_DATA.clear()
    }

    companion object {
        private var INSTANCE: MovieRemoteDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, moviesDao: MoviesDao): MovieRemoteDataSource {
            if (INSTANCE == null) {
                synchronized(MovieRemoteDataSource::javaClass) {
                    INSTANCE = MovieRemoteDataSource(appExecutors, moviesDao)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }
}