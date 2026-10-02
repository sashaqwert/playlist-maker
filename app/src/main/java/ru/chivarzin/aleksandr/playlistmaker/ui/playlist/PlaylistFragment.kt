package ru.chivarzin.aleksandr.playlistmaker.ui.playlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.PlaylistPresentation
import ru.chivarzin.aleksandr.playlistmaker.presentation.playlist.PlaylistViewNodel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PLAYLIST = "playlist"

/**
 * A simple [Fragment] subclass.
 * Use the [PlaylistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlaylistFragment : Fragment() {
    private var playlist: PlaylistPresentation? = null
    private val playlistViewModel: PlaylistViewNodel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            playlist = it.getParcelable(ARG_PLAYLIST, PlaylistPresentation::class.java)
            playlistViewModel.setPlaylist(playlist!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param playlist Parameter 1.
         * @return A new instance of fragment PlaylistFragment.
         */
        @JvmStatic
        fun newInstance(playlist: PlaylistPresentation) =
            PlaylistFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PLAYLIST, playlist)
                }
            }

        fun createArgs(playlist: PlaylistPresentation): Bundle =
            bundleOf(ARG_PLAYLIST to playlist)
    }
}