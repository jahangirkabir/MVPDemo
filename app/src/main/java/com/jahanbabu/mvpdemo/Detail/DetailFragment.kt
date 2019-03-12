package com.example.android.architecture.blueprints.todoapp.taskdetail

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.jahanbabu.mvpdemo.Detail.DetailContract
import com.jahanbabu.mvpdemo.R
import kotlinx.android.synthetic.main.detail_fragment.*

/**
 * Main UI for the task detail screen.
 */
class DetailFragment : Fragment(), DetailContract.View {

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

    override lateinit var presenter: DetailContract.Presenter

    override var isActive: Boolean = false
        get() = isAdded

    override fun onResume() {
        super.onResume()
        presenter.start()
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

        // Set up floating action button
        playButton.setOnClickListener {
            presenter.playVideo()
        }

        return root
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
