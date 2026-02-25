package ru.chivarzin.aleksandr.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
                Toast.makeText(this@MainActivity, "Нажата кнопка поиска", Toast.LENGTH_SHORT).show()
            }
        }
        button_search.setOnClickListener(searchClickListener)

        button_mediateka.setOnClickListener { view ->
            Toast.makeText(this@MainActivity, "Нажата кнопка \"Медиатека\"", Toast.LENGTH_SHORT).show()
        }
        button_settings.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажата кнопка \"Настройки\"", Toast.LENGTH_SHORT).show()
        }
    }
}