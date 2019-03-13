package com.jahanbabu.mvpdemo.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.jahanbabu.mvpdemo.Data.Movie
import com.jahanbabu.mvpdemo.R

/**
 * Main UI for the task detail screen.
 */
class DetailFragment : Fragment(), DetailContract.View, MovieRVAdapter.ItemClickListener {
    override fun onItemClicked(position: Int, id: String) {
        Toast.makeText(activity, "Title: " + movies[position].title, Toast.LENGTH_LONG).show()

//        val intent = Intent(activity, DetailActivity::class.java)
//        intent.putExtra("MOVIE_ID", movies[position].id)
//        startActivity(intent)
//        activity!!.finish()
    }

    override fun setDataToRecyclerView(movieArrayList: List<Movie>) {
        movies.clear()
        movies.addAll(movieArrayList)

        val adapter = MovieRVAdapter(activity!!.applicationContext, movieArrayList as MutableList<Movie>)
        adapter.setClickListener(this)
        relatedRecyclerView.setAdapter(adapter)
    }

    override fun playMovie(url: String) {
        thumbImageView.visibility = View.GONE
        playButton.visibility = View.GONE

        val mediaSource = buildMediaSource(Uri.parse(url))
        player!!.prepare(mediaSource, true, false)
        player!!.playWhenReady = true
    }

    override fun setMovie(url: String) {
        initializePlayer(url)
    }

    override fun showThumb(thumb: String) {
        Glide.with(activity!!).load(thumb)
            .placeholder(R.drawable.ic_image_placeholder).dontAnimate()
            .into(thumbImageView)
    }

    private lateinit var detailTitle: TextView
    private lateinit var detailDescription: TextView
    private lateinit var playButton: Button
    private lateinit var thumbImageView: ImageView
    private var player: SimpleExoPlayer? = null
    private lateinit var playerView: SimpleExoPlayerView
    private lateinit var relatedRecyclerView: RecyclerView
    var movies = mutableListOf<Movie>()
    override lateinit var presenter: DetailContract.Presenter

    override var isActive: Boolean = false
        get() = isAdded

    override fun onResume() {
        super.onResume()
        presenter.start()
//        presenter.requestMoviesFromLocal()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.detail_fragment, container, false)

        with(root) {
            detailTitle = findViewById(R.id.titleTextView)
            detailDescription = findViewById(R.id.descriptionTextView)
            playButton = findViewById(R.id.playButton)
            playerView = findViewById(R.id.video_view)
            thumbImageView = findViewById(R.id.thumbImageView)
            relatedRecyclerView = findViewById(R.id.relatedRecyclerView)
        }

        initializeRecyclerView()
        // Set up floating action button
        playButton.setOnClickListener {
            presenter.playVideo()
        }

        return root
    }

    private fun initializeRecyclerView() {
        val layoutManager = LinearLayoutManager(activity)
        relatedRecyclerView.layoutManager = layoutManager
    }

    private fun initializePlayer(url: String) {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(activity!!.applicationContext),
                DefaultTrackSelector(),
                DefaultLoadControl())
            playerView.setPlayer(player)
//            player!!.setPlayWhenReady(false)
//            player.seekTo(currentWindow, playbackPosition)
        }
//        val mediaSource = buildMediaSource(Uri.parse(url))
//        player!!.prepare(mediaSource, true, false)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {

        val userAgent = "exoplayer-codelab"

        if (uri.getLastPathSegment().contains("mp3") || uri.getLastPathSegment().contains("mp4")) {
            return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri)
        } else if (uri.getLastPathSegment().contains("m3u8")) {
            return HlsMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri)
        } else {
            val dashChunkSourceFactory = DefaultDashChunkSource.Factory(
                DefaultHttpDataSourceFactory("ua", DefaultBandwidthMeter()))
            val manifestDataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
            return DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory).createMediaSource(uri)
        }
    }

    override fun setLoadingIndicator(active: Boolean) {
        if (active) {

        }
    }

    override fun showDescription(description: String) {
        with(detailDescription) {
            visibility = View.VISIBLE
            text = description
        }
    }

    override fun showTitle(title: String) {
        with(detailTitle) {
            visibility = View.VISIBLE
            text = title
        }
    }

    companion object {

        private val ARGUMENT_MOVIE_ID = "MOVIE_ID"

        private val REQUEST_EDIT_TASK = 1

        fun newInstance(taskId: String?) =
                DetailFragment().apply {
                    arguments = Bundle().apply { putString(ARGUMENT_MOVIE_ID, taskId) }
                }
    }

}
