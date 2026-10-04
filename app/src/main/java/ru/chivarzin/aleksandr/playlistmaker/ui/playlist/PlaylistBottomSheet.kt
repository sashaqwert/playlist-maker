package ru.chivarzin.aleksandr.playlistmaker.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.PlaylistPresentation

class PlaylistBottomSheet : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val playlist = it.getParcelable("arg_playlist", PlaylistPresentation::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Используем ту же разметку, которую мы вырезали из основного XML
        return inflater.inflate(R.layout.playlist_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        const val TAG = "PlaylistBottomSheet"

        fun newInstance(playlist: PlaylistPresentation): PlaylistBottomSheet {
            return PlaylistBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable("arg_playlist", playlist)
                }
            }
        }
    }
}