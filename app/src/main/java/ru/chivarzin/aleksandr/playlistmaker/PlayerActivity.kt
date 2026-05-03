package ru.chivarzin.aleksandr.playlistmaker

import android.icu.text.SimpleDateFormat
import android.os.Bundle
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
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var track: Track

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
    }
}