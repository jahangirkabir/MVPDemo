package com.jahanbabu.mvpdemo.Data.Source

import com.jahanbabu.mvpdemo.Data.Movie
import java.util.ArrayList
import java.util.LinkedHashMap

class MovieRepository(val movieRemoteDataSource: MovieDataSource, val movieLocalDataSource: MovieDataSource): MovieDataSource {

    /**
     * This variable has public visibility so it can be accessed from tests.
     */
    var cachedMovies: LinkedHashMap<String, Movie> = LinkedHashMap()

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    var cacheIsDirty = false

    /**
     * Gets movies from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     *
     *
     * Note: [MovieDataSource.LoadMoviesCallback.onDataNotAvailable] is fired if all data sources fail to
     * get the data.
     */
    override fun getMovies(callback: MovieDataSource.LoadMoviesCallback) {
        // Respond immediately with cache if available and not dirty
        if (cachedMovies.isNotEmpty() && !cacheIsDirty) {
            callback.onMovieLoaded(ArrayList(cachedMovies.values))
            return
        }

        if (cacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getMoviesFromRemoteDataSource(callback)
        } else {
            // Query the local storage if available. If not, query the network.
            movieLocalDataSource.getMovies(object : MovieDataSource.LoadMoviesCallback {
                override fun onMovieLoaded(movies: List<Movie>) {
                    refreshCache(movies)
                    callback.onMovieLoaded(ArrayList(cachedMovies.values))
                }

                override fun onDataNotAvailable() {
                    getMoviesFromRemoteDataSource(callback)
                }
            })
        }
    }

    override fun updateMovie(movieId: String) {
        getMovieWithId(movieId)?.let {
//            activateMovie(it)
        }
    }

    override fun saveMovie(movie: Movie) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(movie) {
            movieRemoteDataSource.saveMovie(it)
            movieLocalDataSource.saveMovie(it)
        }
    }

    /**
     * Gets movies from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     *
     *
     * Note: [MovieDataSource.GetMovieCallback.onDataNotAvailable] is fired if both data sources fail to
     * get the data.
     */
    override fun getMovie(movieId: String, callback: MovieDataSource.GetMovieCallback) {
        val movieInCache = getMovieWithId(movieId)

        // Respond immediately with cache if available
        if (movieInCache != null) {
            callback.onMovieLoaded(movieInCache)
            return
        }

        // Load from server/persisted if needed.

        // Is the movie in the local data source? If not, query the network.
        movieLocalDataSource.getMovie(movieId, object : MovieDataSource.GetMovieCallback {
            override fun onMovieLoaded(movie: Movie) {
                // Do in memory cache update to keep the app UI up to date
                cacheAndPerform(movie) {
                    callback.onMovieLoaded(it)
                }
            }

            override fun onDataNotAvailable() {
                movieRemoteDataSource.getMovie(movieId, object : MovieDataSource.GetMovieCallback {
                    override fun onMovieLoaded(movie: Movie) {
                        // Do in memory cache update to keep the app UI up to date
                        cacheAndPerform(movie) {
                            callback.onMovieLoaded(it)
                        }
                    }

                    override fun onDataNotAvailable() {
                        callback.onDataNotAvailable()
                    }
                })
            }
        })
    }

    private fun getMoviesFromRemoteDataSource(callback: MovieDataSource.LoadMoviesCallback) {
        movieRemoteDataSource.getMovies(object : MovieDataSource.LoadMoviesCallback {
            override fun onMovieLoaded(movies: List<Movie>) {
                refreshCache(movies)
                refreshLocalDataSource(movies)
                callback.onMovieLoaded(ArrayList(cachedMovies.values))
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    override fun refreshMovies() {
        cacheIsDirty = true
    }

    override fun deleteAllMovies() {
        movieRemoteDataSource.deleteAllMovies()
        movieLocalDataSource.deleteAllMovies()
        cachedMovies.clear()
    }

    private fun refreshCache(movies: List<Movie>) {
        cachedMovies.clear()
        movies.forEach {
            cacheAndPerform(it) {}
        }
        cacheIsDirty = false
    }

    private fun refreshLocalDataSource(movies: List<Movie>) {
        movieLocalDataSource.deleteAllMovies()
        for (movie in movies) {
            movieLocalDataSource.saveMovie(movie)
        }
    }

    private fun getMovieWithId(id: String) = cachedMovies[id]

    private inline fun cacheAndPerform(movie: Movie, perform: (Movie) -> Unit) {
        val cachedMovie = Movie(movie.id, movie.title, movie.description, movie.thumb, movie.url).apply {
            id = movie.id
            title = movie.title
            description = movie.description
            thumb = movie.thumb
            url = movie.url
        }
        cachedMovies.put(cachedMovie.id, cachedMovie)
        perform(cachedMovie)
    }

    companion object {

        private var INSTANCE: MovieRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.

         * @param movieRemoteDataSource the backend data source
         * *
         * @param movieLocalDataSource  the device storage data source
         * *
         * @return the [MovieRepository] instance
         */
        @JvmStatic fun getInstance(movieRemoteDataSource: MovieDataSource,
                                   movieLocalDataSource: MovieDataSource
        ): MovieRepository {
            return INSTANCE ?: MovieRepository(movieRemoteDataSource, movieLocalDataSource)
                    .apply { INSTANCE = this }
        }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}