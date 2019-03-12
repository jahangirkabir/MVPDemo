package com.jahanbabu.mvpdemo.Data.Source.local

import androidx.annotation.VisibleForTesting
import com.jahanbabu.mvpdemo.Data.Movie
import com.jahanbabu.mvpdemo.Data.Source.MovieDataSource
import com.jahanbabu.mvpdemo.util.AppExecutors

/**
 * Concrete implementation of a data source as a db.
 */
class MovieLocalDataSource private constructor(
    val appExecutors: AppExecutors,
    val moviesDao: MoviesDao
) : MovieDataSource {

    /**
     * Note: [MovieDataSource.LoadMoviesCallback.onDataNotAvailable] is fired if the database doesn't exist
     * or the table is empty.
     */
    override fun getMovies(callback: MovieDataSource.LoadMoviesCallback) {
        appExecutors.diskIO.execute {
            val tasks = moviesDao.getMovies()
            appExecutors.mainThread.execute {
                if (tasks.isEmpty()) {
                    // This will be called if the table is new or just empty.
                    callback.onDataNotAvailable()
                } else {
                    callback.onMovieLoaded(tasks)
                }
            }
        }
    }

    /**
     * Note: [MovieDataSource.GetMovieCallback.onDataNotAvailable] is fired if the [Movie] isn't
     * found.
     */
    override fun getMovie(movieId: String, callback: MovieDataSource.GetMovieCallback) {
        appExecutors.diskIO.execute {
            val movie = moviesDao.getMovieById(movieId)
            appExecutors.mainThread.execute {
                if (movie != null) {
                    callback.onMovieLoaded(movie)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    override fun saveMovie(movie: Movie) {
        appExecutors.diskIO.execute { moviesDao.insertMovie(movie) }
    }

    override fun updateMovie(movieId: String) {
        // Not required for the local data source because the {@link MovieRepository} handles
        // converting from a {@code movieId} to a {@link movie} using its cached data.
    }

    override fun refreshMovies() {
        // Not required because the {@link MovieRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    override fun deleteAllMovies() {
        appExecutors.diskIO.execute { moviesDao.deleteMovies() }
    }

    companion object {
        private var INSTANCE: MovieLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, moviesDao: MoviesDao): MovieLocalDataSource {
            if (INSTANCE == null) {
                synchronized(MovieLocalDataSource::javaClass) {
                    INSTANCE = MovieLocalDataSource(appExecutors, moviesDao)
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
