package ru.chivarzin.aleksandr.playlistmaker.ui.player

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track
import ru.chivarzin.aleksandr.playlistmaker.dpToPx
import ru.chivarzin.aleksandr.playlistmaker.isDarkTheme
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation
import ru.chivarzin.aleksandr.playlistmaker.presentation.player.PlayerState
import ru.chivarzin.aleksandr.playlistmaker.presentation.player.PlayerViewModel
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private val playerViewModel: PlayerViewModel by viewModel {
        parametersOf(track)
    }
    private lateinit var track: TrackPresentation
    private lateinit var player_playpause: ImageView
    private lateinit var player_progress: TextView

    private lateinit var player_artwork: ImageView
    private lateinit var player_track_name: TextView
    private lateinit var player_artist_name: TextView
    private lateinit var player_duration: TextView
    private lateinit var player_collection_hint: TextView
    private lateinit var player_collection_name: TextView
    private lateinit var player_release_date_hint: TextView
    private lateinit var player_release_date: TextView
    private lateinit var player_janr: TextView
    private lateinit var player_country: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        track = intent.getParcelableExtra("track", TrackPresentation::class.java)!! //min API 33
        //track = intent.getParcelableExtra("track")
        //track = Gson().fromJson<Track>(intent.getStringExtra("track"), Track::class.java)
        val player_action_back = findViewById<ImageView>(R.id.player_action_back)
        player_action_back.setOnClickListener {
            finish()
        }

        player_artwork = findViewById<ImageView>(R.id.player_artwork)
        player_track_name = findViewById<TextView>(R.id.player_track_name)
        player_artist_name = findViewById<TextView>(R.id.player_artist_name)
        player_duration = findViewById<TextView>(R.id.player_duration)
        player_collection_hint = findViewById<TextView>(R.id.player_collection_hint)
        player_collection_name = findViewById<TextView>(R.id.player_collection_name)
        player_release_date_hint = findViewById<TextView>(R.id.player_release_date_hint)
        player_release_date = findViewById<TextView>(R.id.player_release_date)
        player_janr = findViewById<TextView>(R.id.player_janr)
        player_country = findViewById<TextView>(R.id.player_country)
        player_playpause = findViewById<ImageView>(R.id.player_playpause)
        player_progress = findViewById<TextView>(R.id.player_progress)

        playerViewModel.obsorveUiState().observe(this) {
            render(it)
        }

        playerViewModel.observePlayerState().observe(this) {
            //player_state_changed(it)
        }
        playerViewModel.observeProgressTime().observe(this) {
            //update_progress(it)
        }
        player_playpause.setOnClickListener {
            playerViewModel.onPlayButtonClicked()
        }
    }

    fun notPreparedToast() {
        Toast.makeText(applicationContext, R.string.player_not_prepared, Toast.LENGTH_SHORT).show()
    }

    private fun initialize(track: TrackPresentation) {
        if (track.artworkUrl100 != null) {
            Glide.with(this)
                .load(track.getCoverArtwork())
                .fitCenter()
                .transform(RoundedCorners(dpToPx(8.0f, this)))
                .placeholder(R.drawable.artwork_default)
                .into(player_artwork)
        }
        if (track.trackName != null) {
            player_track_name.setText(track.trackName)
        }
        if (track.artistName != null) {
            player_artist_name.setText(track.artistName)
        }
        if (track.trackTimeMillis != null) {
            player_duration.setText(SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis))
        }
        if (track.collectionName == null) {
            player_collection_hint.visibility = View.GONE
            player_collection_name.visibility = View.GONE
        } else {
            player_collection_name.setText(track.collectionName)
        }
        if (track.releaseDate == null) {
            player_release_date_hint.visibility = View.GONE
            player_release_date.visibility = View.GONE
        } else {
            player_release_date.setText(track.getYear()!!.toString())
        }
        if (track.primaryGenreName != null) {
            player_janr.setText(track.primaryGenreName)
        }
        if (track.country != null) {
            player_country.setText(track.country)
        }
    }

    private fun player_state_changed(state: Int) {
        when (state) {
            // Оставлено для понимания.
            // Если раскомментировать, будет сломка макета
//            PlayerViewModel.STATE_DEFAULT -> if (isDarkTheme(this)) {
//                Glide.with(this)
//                    .load(R.drawable.play_dark)
//                    .fitCenter()
//                    .into(player_playpause)
//            } else {
//                Glide.with(this)
//                    .load(R.drawable.play)
//                    // .fitCenter()
//                    .into(player_playpause)
//            }

            PlayerViewModel.STATE_PREPARED -> if (isDarkTheme(this)) {
                Glide.with(this)
                    .load(R.drawable.play_dark)
                    // .fitCenter()
                    .into(player_playpause)
            } else {
                Glide.with(this)
                    .load(R.drawable.play)
                    .fitCenter()
                    .into(player_playpause)
            }

            PlayerViewModel.STATE_PLAYING -> if (isDarkTheme(this)) {
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

            PlayerViewModel.STATE_PAUSED -> if (isDarkTheme(this)) {
                Glide.with(this)
                    .load(R.drawable.play_dark)
                    //  .fitCenter()
                    .into(player_playpause)
            } else {
                Glide.with(this)
                    .load(R.drawable.play)
                    .fitCenter()
                    .into(player_playpause)
            }
        }
    }

    private fun update_progress(progress: String) {
        player_progress.text = progress
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.onPause()
    }

    fun render (state: PlayerState) {
        when(state) {
            is PlayerState.Initial -> initialize(state.track)
            is PlayerState.State -> player_state_changed(state.player_state)
            is PlayerState.Progress -> update_progress(state.progress)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}