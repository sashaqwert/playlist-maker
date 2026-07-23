package ru.chivarzin.aleksandr.playlistmaker.ui.mediateka

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.presentation.mediateka.PlaylistsViewModel

class PlaylistsFragment : Fragment() {
    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlists, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlaylistsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}