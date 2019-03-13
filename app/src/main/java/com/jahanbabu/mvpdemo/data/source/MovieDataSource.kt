package com.jahanbabu.mvpdemo.data.source

import com.jahanbabu.mvpdemo.data.Movie

interface MovieDataSource {

    interface LoadMoviesCallback {

        fun onMovieLoaded(movies: List<Movie>)

        fun onDataNotAvailable()
    }

    interface GetMovieCallback {

        fun onMovieLoaded(movie: Movie)

        fun onDataNotAvailable()
    }

    fun saveMovie(movie: Movie)

    fun getMovies(callback: LoadMoviesCallback)

    fun getRelatedMovies(title: String, callback: LoadMoviesCallback)

    fun getMovie(movieId: String, callback: GetMovieCallback)

    fun updateMovie(movieId: String, playbackPosition: Long)

    fun refreshMovies()

    fun deleteAllMovies()

}