package com.jahanbabu.mvpdemo.Data.Source

import com.jahanbabu.mvpdemo.Data.Movie

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

    fun getMovie(movieId: String, callback: GetMovieCallback)

    fun updateMovie(taskId: String)

    fun refreshMovies()

    fun deleteAllMovies()

}