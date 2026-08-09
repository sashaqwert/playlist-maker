package ru.chivarzin.aleksandr.playlistmaker.ui.mediateka

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.chivarzin.aleksandr.playlistmaker.R


class MediatekaFragment : Fragment() {
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mediateka, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout_mediateka)
        val viewPager2 = view.findViewById<ViewPager2>(R.id.viewPager_mediateka)

        viewPager2.adapter = MediatekaViewPagerAdapter(
            fragmentManager = childFragmentManager,
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

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MediatekaFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}