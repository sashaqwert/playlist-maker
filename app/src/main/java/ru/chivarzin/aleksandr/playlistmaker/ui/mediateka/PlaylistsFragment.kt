package ru.chivarzin.aleksandr.playlistmaker.ui.mediateka

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist
import ru.chivarzin.aleksandr.playlistmaker.presentation.mediateka.PlaylistsState
import ru.chivarzin.aleksandr.playlistmaker.presentation.mediateka.PlaylistsViewModel
import ru.chivarzin.aleksandr.playlistmaker.ui.adapters.PlaylistAdapter

class PlaylistsFragment : Fragment() {
    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    private var new_playlist_button: Button? = null
    private var playlist_list: RecyclerView? = null
    private var playlists_pb: ProgressBar? = null
    private var icon_error: ImageView? = null
    private var error_text: TextView? = null

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        new_playlist_button = view.findViewById<Button>(R.id.new_playlist_button)
        new_playlist_button?.setOnClickListener {
            findNavController().navigate(R.id.action_mediatekaFragment_to_newPlaylistFragment)
        }
        playlists_pb = view.findViewById<ProgressBar>(R.id.playlists_pb)
        playlist_list = view.findViewById<RecyclerView>(R.id.playlist_list)
        playlist_list?.layoutManager = GridLayoutManager(
            requireActivity(), /*Количество столбцов*/
            2
        )
        icon_error = view.findViewById<ImageView>(R.id.icon_error)
        error_text = view.findViewById<TextView>(R.id.error_text)

        playlistsViewModel.fillData()
        playlistsViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    fun showLoading() {
        playlist_list?.visibility = View.GONE
        playlists_pb?.visibility = View.VISIBLE
        error_text?.visibility = View.GONE
        icon_error?.visibility = View.GONE
    }

    fun showEmpty() {
        playlist_list?.visibility = View.GONE
        playlists_pb?.visibility = View.GONE
        error_text?.visibility = View.VISIBLE
        icon_error?.visibility = View.VISIBLE
    }

    fun showContent(playlists: List<Playlist>) {
        playlist_list?.visibility = View.VISIBLE
        playlists_pb?.visibility = View.GONE
        error_text?.visibility = View.GONE
        icon_error?.visibility = View.GONE

        val adapter = PlaylistAdapter(playlists)
        playlist_list?.adapter = adapter
    }

    fun render(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Loading -> showLoading()
            is PlaylistsState.Empty -> showEmpty()
            is PlaylistsState.Content -> showContent(state.playlists)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        new_playlist_button = null
        playlist_list?.adapter = null
        playlist_list = null
        playlists_pb = null
        icon_error = null
        error_text = null
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