package com.jahanbabu.mvpdemo

import android.content.Context
import com.jahanbabu.mvpdemo.Data.Source.MovieRepository
import com.jahanbabu.mvpdemo.Data.Source.local.MovieLocalDataSource
import com.jahanbabu.mvpdemo.Data.Source.local.MovieDatabase
import com.jahanbabu.mvpdemo.Data.Source.remote.MovieRemoteDataSource
import com.jahanbabu.mvpdemo.util.AppExecutors


/**
 * Enables injection of mock implementations for
 * [TasksDataSource] at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
object Injection {
    fun provideTasksRepository(context: Context): MovieRepository {
        val database = MovieDatabase.getInstance(context)
        return MovieRepository.getInstance(
            MovieRemoteDataSource.getInstance(AppExecutors(), database.moviesDao()),
                MovieLocalDataSource.getInstance(AppExecutors(), database.moviesDao()))
    }
}
