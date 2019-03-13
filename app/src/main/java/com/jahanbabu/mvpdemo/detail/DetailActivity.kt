package com.jahanbabu.mvpdemo.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jahanbabu.mvpdemo.home.GetMovieIntractorImpl
import com.jahanbabu.mvpdemo.Injection
import com.jahanbabu.mvpdemo.R
import com.jahanbabu.mvpdemo.util.replaceFragmentInActivity

/**
 * Displays task details screen.
 */
class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.detail_activity)


        // Get the requested task id
        val movieId = intent.getStringExtra(EXTRA_MOVIE_ID)

        val taskDetailFragment = supportFragmentManager
                .findFragmentById(R.id.contentFrame) as DetailFragment? ?:
                DetailFragment.newInstance(movieId).also {
                    replaceFragmentInActivity(it, R.id.contentFrame)
                }
        // Create the presenter
        DetailPresenter(movieId, Injection.provideTasksRepository(applicationContext), taskDetailFragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_MOVIE_ID = "MOVIE_ID"
    }
}
