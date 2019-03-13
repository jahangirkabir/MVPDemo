package com.jahanbabu.mvpdemo.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jahanbabu.mvpdemo.data.Movie

/**
 * The Room Database that contains the Movie table.
 */
@Database(entities = [Movie::class], version = 2)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

    companion object {

        private var INSTANCE: MovieDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): MovieDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            MovieDatabase::class.java, "Movies.db").fallbackToDestructiveMigration()
                            .build()
                }
                return INSTANCE!!
            }
        }
    }

}