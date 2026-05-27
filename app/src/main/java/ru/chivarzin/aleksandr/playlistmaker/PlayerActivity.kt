package ru.chivarzin.aleksandr.playlistmaker

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import org.w3c.dom.Text
import java.lang.Thread.sleep
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var track: Track
    private var mediaPlayer = MediaPlayer()
    private lateinit var player_playpause: ImageView
    private lateinit var player_progress: TextView
    private var playerState = STATE_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        track = Gson().fromJson<Track>(intent.getStringExtra("track"), Track::class.java)
        val player_action_back = findViewById<ImageView>(R.id.player_action_back)
        player_action_back.setOnClickListener {
            finish()
        }

        val player_artwork = findViewById<ImageView>(R.id.player_artwork)
        if (track.artworkUrl100 != null) {
            Glide.with(this)
                .load(track.getCoverArtwork())
                .fitCenter()
                .transform(RoundedCorners(dpToPx(8.0f, this)))
                .placeholder(R.drawable.artwork_default)
                .into(player_artwork)
        }
        val player_track_name = findViewById<TextView>(R.id.player_track_name)
        if (track.trackName != null) {
            player_track_name.setText(track.trackName)
        }
        val player_artist_name = findViewById<TextView>(R.id.player_artist_name)
        if (track.artistName != null) {
            player_artist_name.setText(track.artistName)
        }
        val player_duration = findViewById<TextView>(R.id.player_duration)
        if (track.trackTimeMillis != null) {
            player_duration.setText(SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis))
        }
        val player_collection_hint = findViewById<TextView>(R.id.player_collection_hint)
        val player_collection_name = findViewById<TextView>(R.id.player_collection_name)
        if (track.collectionName == null) {
            player_collection_hint.visibility = View.GONE
            player_collection_name.visibility = View.GONE
        } else {
            player_collection_name.setText(track.collectionName)
        }
        val player_release_date_hint = findViewById<TextView>(R.id.player_release_date_hint)
        val player_release_date = findViewById<TextView>(R.id.player_release_date)
        if (track.releaseDate == null) {
            player_release_date_hint.visibility = View.GONE
            player_release_date.visibility = View.GONE
        } else {
            player_release_date.setText(track.getYear()!!.toString())
        }
        val player_janr = findViewById<TextView>(R.id.player_janr)
        if (track.primaryGenreName != null) {
            player_janr.setText(track.primaryGenreName)
        }
        val player_country = findViewById<TextView>(R.id.player_country)
        if (track.country != null) {
            player_country.setText(track.country)
        }

        preparePlayer()
        player_playpause = findViewById<ImageView>(R.id.player_playpause)
        player_playpause.setOnClickListener {
            playbackControl()
        }
        player_progress = findViewById<TextView>(R.id.player_progress)
    }

    private fun preparePlayer() {
        if (track.previewUrl == null) {
            return
        }
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            player_playpause.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            if (isDarkTheme(this)) {
                Glide.with(this)
                    .load(R.drawable.play_dark)
                    .fitCenter()
                    .into(player_playpause)
            } else {
                Glide.with(this)
                    .load(R.drawable.play)
                    .fitCenter()
                    .into(player_playpause)
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        if (isDarkTheme(this)) {
            Glide.with(this)
                .load(R.drawable.pause_dark)
                .fitCenter()
                .into(player_playpause)
        } else {
            Glide.with(this)
                .load(R.drawable.pause)
                .fitCenter()
                .into(player_playpause)
        }

        val handler = Handler(Looper.getMainLooper())
        val thread = Thread({
            do {
                sleep(250L)
                handler.post {
                    player_progress.setText(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition))
                }
            } while (playerState == STATE_PLAYING)
            handler.post {
                if (playerState == STATE_PAUSED) {
                    player_progress.setText(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition))
                }
                else {
                    player_progress.setText(R.string.track_time_default)
                }
            }
        })
        thread.start()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        if (isDarkTheme(this)) {
            Glide.with(this)
                .load(R.drawable.play_dark)
                .fitCenter()
                .into(player_playpause)
        } else {
            Glide.with(this)
                .load(R.drawable.play)
                .fitCenter()
                .into(player_playpause)
        }
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}