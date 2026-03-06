package ru.chivarzin.aleksandr.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.net.toUri

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
//        enableEdgeToEdge()
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        val action_back = findViewById<ImageView>(R.id.settings_action_back)
        action_back.setOnClickListener {
            finish()
        }

        val action_share = findViewById<Button>(R.id.action_share)
        action_share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.course_url))
            startActivity(intent)
        }

        val action_support = findViewById<Button>(R.id.action_support)
        action_support.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = "mailto:".toUri()
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_email_text))
            startActivity(shareIntent)
        }

        val action_user_agreement = findViewById<Button>(R.id.action_user_agreement)
        action_user_agreement.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(getString(R.string.user_agreement_url).toUri())
            startActivity(intent)
        }
    }
}