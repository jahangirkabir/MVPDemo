package com.jahanbabu.mvpdemo.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jahanbabu.mvpdemo.R
import androidx.recyclerview.widget.RecyclerView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import com.jahanbabu.mvpdemo.data.Movie
import com.jahanbabu.mvpdemo.detail.DetailActivity
import com.jahanbabu.mvpdemo.Injection

class MainActivity : AppCompatActivity(), MainContract.View, MovieRVAdapter.ItemClickListener {
    override fun onItemClicked(position: Int, id: String) {
        Toast.makeText(this@MainActivity, "Title: " + movies[position].title, Toast.LENGTH_LONG).show()

        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra("MOVIE_ID", movies[position].id)
        startActivity(intent)
    }

    override fun showProgress() {
        progressBar!!.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar!!.visibility = View.GONE
    }

    override fun setDataToRecyclerView(movieArrayList: ArrayList<Movie>) {
        movies.clear()
        movies.addAll(movieArrayList)

        presenter.saveMovies(movieArrayList)

        val adapter = MovieRVAdapter(this@MainActivity, movieArrayList)
        adapter.setClickListener(this@MainActivity)
        recyclerView!!.setAdapter(adapter)
    }

    override fun onResponseFailure(throwable: Throwable) {

    }

    override fun navigateToDetailScreen(complete: Boolean) {

    }

    var progressBar: ProgressBar? = null
    var recyclerView: RecyclerView? = null
    var movies = mutableListOf<Movie>()
    override lateinit var presenter: MainContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeRecyclerView()
        initProgressBar()


        presenter = MainPresenter(this, GetMovieIntractorImpl(), Injection.provideTasksRepository(applicationContext))
        presenter.requestDataFromServer()
    }

    /**
     * Initializing Toolbar and RecyclerView
     */
    private fun initializeRecyclerView() {

//        val toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.videoRecyclerView)
        val layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView!!.layoutManager = layoutManager
    }

    /**
     * Initializing progressbar programmatically
     */
    private fun initProgressBar() {
        progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleLarge)
        progressBar!!.isIndeterminate = true

        val relativeLayout = RelativeLayout(this)
        relativeLayout.gravity = Gravity.CENTER
        relativeLayout.addView(progressBar)

        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        progressBar!!.visibility = View.INVISIBLE

        this.addContentView(relativeLayout, params)
    }
}
