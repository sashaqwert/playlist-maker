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
import ru.chivarzin.aleksandr.playlistmaker.presentation.player.PlayerViewModel
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private val playerViewModel: PlayerViewModel by viewModel {
        parametersOf(track)
    }
    private lateinit var track: Track
    private lateinit var player_playpause: ImageView
    private lateinit var player_progress: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        track = intent.getParcelableExtra("track", Track::class.java)!! //min API 33
        //track = intent.getParcelableExtra("track")
        //track = Gson().fromJson<Track>(intent.getStringExtra("track"), Track::class.java)
        val player_action_back = findViewById<ImageView>(R.id.player_action_back)
        player_action_back.setOnClickListener {
            finish()
        }

        val player_artwork = findViewById<ImageView>(R.id.player_artwork)

        playerViewModel.observeTrack().observe(this) { track ->
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
        }

        player_playpause = findViewById<ImageView>(R.id.player_playpause)
        player_progress = findViewById<TextView>(R.id.player_progress)

        playerViewModel.observePlayerState().observe(this) {
            when (it) {
                // Оставлено для понимания.
                // Если раскомментировать, будет сломка макета
                //PlayerViewModel.STATE_DEFAULT -> if (isDarkTheme(this)) {
//                    Glide.with(this)
//                        .load(R.drawable.play_dark)
//                        .fitCenter()
//                        .into(player_playpause)
//                } else {
//                    Glide.with(this)
//                        .load(R.drawable.play)
//                       // .fitCenter()
//                        .into(player_playpause)
//                }

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
                       // .fitCenter()
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
                     //   .fitCenter()
                        .into(player_playpause)
                }
            }
        }
        playerViewModel.observeProgressTime().observe(this) {
            player_progress.text = it
        }
        player_playpause.setOnClickListener {
            playerViewModel.onPlayButtonClicked()
        }
    }

    fun notPreparedToast() {
        Toast.makeText(applicationContext, R.string.player_not_prepared, Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}