package com.jahanbabu.mvpdemo.Data.Source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jahanbabu.mvpdemo.Data.Movie

/**
 * Data Access Object for the movies table.
 */
@Dao interface MoviesDao {

    /**
     * Select all movies from the movies table.
     *
     * @return all movies.
     */
    @Query("SELECT * FROM Movies") fun getMovies(): List<Movie>

    /**
     * Select a movie by id.
     *
     * @param movieId the movie id.
     * @return the movie with movieId.
     */
    @Query("SELECT * FROM Movies WHERE id = :movieId") fun getMovieById(movieId: String): Movie?

    /**
     * Insert a movie in the database. If the movie already exists, replace it.
     *
     * @param movie the movie to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertMovie(movie: Movie)

    /**
     * Update a movie.
     *
     * @param movie movie to be updated
     * @return the number of movies updated. This should always be 1.
     */
    @Update fun updateMovie(movie: Movie): Int

    /**
     * Update the complete status of a movie
     *
     * @param movieId    id of the movie
     * @param completed status to be updated
     */
    @Query("UPDATE Movies SET position = :position WHERE id = :movieId")
    fun updateViewPosition(movieId: String, position: Float)
    
    /**
     * Delete all movies.
     */
    @Query("DELETE FROM Movies") fun deleteMovies()

}