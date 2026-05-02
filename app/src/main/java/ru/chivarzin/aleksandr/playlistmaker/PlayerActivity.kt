package ru.chivarzin.aleksandr.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson

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
        Glide.with(this)
            .load(track.getCoverArtwork())
            .fitCenter()
            .placeholder(R.drawable.artwork_default)
            .into(player_artwork)
    }
}