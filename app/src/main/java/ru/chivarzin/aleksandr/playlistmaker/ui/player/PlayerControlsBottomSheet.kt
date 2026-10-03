package ru.chivarzin.aleksandr.playlistmaker.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.PlaylistPresentation
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation
import ru.chivarzin.aleksandr.playlistmaker.presentation.player.DialogState
import ru.chivarzin.aleksandr.playlistmaker.presentation.player.PlayerControlsBottomSheetViewModel
import ru.chivarzin.aleksandr.playlistmaker.ui.adapters.OnPlaylistClickCallback
import ru.chivarzin.aleksandr.playlistmaker.ui.adapters.PlaylistAdapter
import ru.chivarzin.aleksandr.playlistmaker.ui.newplaylist.NewPlaylistFragment

class PlayerControlsBottomSheet : BottomSheetDialogFragment() {
    private lateinit var track: TrackPresentation
    private val playerControlsBottomSheetViewModel: PlayerControlsBottomSheetViewModel by viewModel {
        parametersOf(track)
    }
    private var pb: ProgressBar? = null
    private var playlists: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val track = it.getParcelable("arg_track", TrackPresentation::class.java)
            this.track = track!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Используем ту же разметку, которую мы вырезали из основного XML
        return inflater.inflate(R.layout.player_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val new_playlist_button = view.findViewById<Button>(R.id.new_playlist_button)
        new_playlist_button.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment,
                NewPlaylistFragment.createArgs(track))
            dismiss()
        }
        pb = view.findViewById<ProgressBar>(R.id.pb)
        playlists = view.findViewById<RecyclerView>(R.id.playlists)
        playerControlsBottomSheetViewModel.fillData()
        playerControlsBottomSheetViewModel.observeUiState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    fun show_loading() {
        pb?.visibility = View.VISIBLE
        playlists?.visibility = View.GONE
    }

    fun show_content(playlists_: List<PlaylistPresentation>) {
        pb?.visibility = View.GONE
        playlists?.visibility = View.VISIBLE

        val adapter = PlaylistAdapter(playlists_, object : OnPlaylistClickCallback {
            override fun callback(playlist: PlaylistPresentation) {
                playerControlsBottomSheetViewModel.add_track_to_playlist(playlist)
            }
        })
        playlists?.adapter = adapter
    }

    fun added(playlist_name: String) {
        Toast.makeText(activity?.applicationContext, "${activity?.getString(R.string.added_to_playlist)} ${playlist_name}", Toast.LENGTH_LONG).show()
        dismiss()
    }

    fun already_added(playlist_name: String) {
        Toast.makeText(activity?.applicationContext, "${activity?.getString(R.string.already_added_to_playlist)} ${playlist_name}", Toast.LENGTH_LONG).show()
    }

    fun render(state: DialogState) {
        when(state) {
            is DialogState.Loading -> show_loading()
            is DialogState.Content -> show_content(state.playlists)
            is DialogState.Added -> added(state.playlist_name)
            is DialogState.AlreadyAdded -> already_added(state.playlist_name)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pb = null
        playlists?.adapter = null
        playlists = null
    }

    companion object {
        const val TAG = "PlayerControlsBottomSheet"

        fun newInstance(track: TrackPresentation): PlayerControlsBottomSheet {
            return PlayerControlsBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable("arg_track", track)
                }
            }
        }
    }
}