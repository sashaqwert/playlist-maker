package ru.chivarzin.aleksandr.playlistmaker.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation
import ru.chivarzin.aleksandr.playlistmaker.ui.newplaylist.NewPlaylistFragment

class PlayerControlsBottomSheet : BottomSheetDialogFragment() {
    private lateinit var track: TrackPresentation

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