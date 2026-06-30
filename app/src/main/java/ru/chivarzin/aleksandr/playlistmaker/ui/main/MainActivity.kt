package ru.chivarzin.aleksandr.playlistmaker.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.ui.mediateka.MediatekaActivity
import ru.chivarzin.aleksandr.playlistmaker.ui.search.SearchActivity
import ru.chivarzin.aleksandr.playlistmaker.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val button_search = findViewById<Button>(R.id.button_search)
        val button_mediateka = findViewById<Button>(R.id.button_mediateka)
        val button_settings = findViewById<Button>(R.id.button_settings)

        val searchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        button_search.setOnClickListener(searchClickListener)

        button_mediateka.setOnClickListener { view ->
            val intent = Intent(this@MainActivity, MediatekaActivity::class.java)
            startActivity(intent)
        }
        button_settings.setOnClickListener {
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}