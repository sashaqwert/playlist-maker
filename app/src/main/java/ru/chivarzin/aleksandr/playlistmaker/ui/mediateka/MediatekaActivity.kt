package ru.chivarzin.aleksandr.playlistmaker.ui.mediateka

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.chivarzin.aleksandr.playlistmaker.R

class MediatekaActivity : AppCompatActivity() {
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mediateka)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val action_back = findViewById<ImageView>(R.id.mediateka_action_back)
        action_back.setOnClickListener {
            finish()
        }
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout_mediateka)
        val viewPager2 = findViewById<ViewPager2>(R.id.viewPager_mediateka)

        viewPager2.adapter = MediatekaViewPagerAdapter(
            fragmentManager = supportFragmentManager,
            lifecycle = lifecycle
        )
        tabMediator = TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}