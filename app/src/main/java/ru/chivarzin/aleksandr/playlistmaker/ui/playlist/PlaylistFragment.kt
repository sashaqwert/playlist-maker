package ru.chivarzin.aleksandr.playlistmaker.ui.playlist

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.PlaylistPresentation
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation
import ru.chivarzin.aleksandr.playlistmaker.presentation.playlist.PlaylistViewNodel
import ru.chivarzin.aleksandr.playlistmaker.ui.adapters.callback.OnTrackClickCallback
import ru.chivarzin.aleksandr.playlistmaker.ui.adapters.TrackAdapter
import ru.chivarzin.aleksandr.playlistmaker.ui.adapters.callback.OnTrackLongClickCallback
import ru.chivarzin.aleksandr.playlistmaker.ui.editplaylist.EditPlaylistFragment
import ru.chivarzin.aleksandr.playlistmaker.ui.player.PlayerFragment
import java.util.Locale

private const val ARG_PLAYLIST = "playlist"

/**
 * A simple [Fragment] subclass.
 * Use the [PlaylistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlaylistFragment : Fragment() {
    private var playlist: PlaylistPresentation? = null
    private val playlistViewModel: PlaylistViewNodel by viewModel()
    private var playlist_name: TextView? = null
    private var playlist_artwork: ImageView? = null
    private var playlist_description: TextView? = null
    private var playlist_time: TextView? = null
    private var playlist_track_count: TextView? = null
    private var playlist_tracks: RecyclerView? = null
    private var playlist_share: ImageView? = null
    private var playlist_menu: ImageView? = null

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
        val playlist_action_back = view.findViewById<ImageView>(R.id.playlist_action_back)
        playlist_action_back.setOnClickListener {
            findNavController().navigateUp()
        }
        playlist_name = view.findViewById<TextView>(R.id.playlist_name)
        playlist_description = view.findViewById<TextView>(R.id.playlist_description)
        playlist_artwork = view.findViewById<ImageView>(R.id.playlist_artwork)
        playlist_time = view.findViewById<TextView>(R.id.playlist_time)
        playlist_track_count = view.findViewById<TextView>(R.id.playlist_track_count)
        playlist_tracks = view.findViewById<RecyclerView>(R.id.playlist_tracks)
        playlist_share = view.findViewById<ImageView>(R.id.playlist_share)
        playlist_menu = view.findViewById<ImageView>(R.id.playlist_menu)

        playlistViewModel.observeState().observe(viewLifecycleOwner) {
            playlist_name?.setText(it.name)
            playlist_description?.setText(it.description)
            if (it.artwork_path != "") {
                Glide.with(this)
                    .load(it.artwork_path.toUri())
                    .centerCrop()
                    .into(playlist_artwork!!)
            }
            playlist_time?.setText("${it.total_time()} ${getString(R.string.minut)}")
            playlist_track_count?.setText("${it.tracks_count} ${getString(R.string.treka)}")
            val adapter = TrackAdapter((ArrayList<TrackPresentation>(it.tracks)), object : OnTrackClickCallback {
                override fun callback(track: TrackPresentation) {
                    findNavController().navigate(R.id.action_playlistFragment_to_playerFragment,
                        PlayerFragment.createArgs(track))
                }
            }, object : OnTrackLongClickCallback {
                override fun callback(track: TrackPresentation) {
                    MaterialAlertDialogBuilder(requireActivity())
                        .setMessage(getString(R.string.want_to_remove_track)) // Описание диалога
                        .setNegativeButton(getString(R.string.no)) { dialog, which -> // Добавляет кнопку «Нет»
                            // Действия, выполняемые при нажатии на кнопку «Нет»
                        }
                        .setPositiveButton(getString(R.string.yes)) { dialog, which -> // Добавляет кнопку «Да»
                            // Действия, выполняемые при нажатии на кнопку «Да»
                            playlistViewModel.removeTrack(track)
                        }
                        .show()
                }
            })
            playlist_tracks?.adapter = adapter
            playlist_share?.setOnClickListener { v ->
                share(it)
            }
            playlist_menu?.setOnClickListener { v ->
                val bottomSheet = PlaylistBottomSheet.newInstance(it)
                bottomSheet.show(parentFragmentManager, PlaylistBottomSheet.TAG)
            }
        }

        // В onViewCreated, после инициализации вьюх
        parentFragmentManager.setFragmentResultListener("playlist_sheet_request", viewLifecycleOwner) { requestKey, bundle ->
            val action = bundle.getString("action")
            val updatedPlaylist = bundle.getParcelable("playlist", PlaylistPresentation::class.java)

            if (action != null) {
                when (action) {
                    "edit" -> {
                        findNavController().navigate(R.id.action_playlistFragment_to_editPlaylistFragment,
                            EditPlaylistFragment.createArgs(updatedPlaylist!!))
                    }
                    "delete" -> {
                        MaterialAlertDialogBuilder(requireActivity())
                            .setTitle(getString(R.string.remove_playlist))
                            .setMessage(getString(R.string.want_to_delete_playlist) + "«" + playlist!!.name + "»?") // Описание диалога
                            .setNeutralButton(getString(R.string.cancel)) { dialog, which -> // Добавляет кнопку «Нет»
                                // Действия, выполняемые при нажатии на кнопку «Нет»
                            }
                            .setPositiveButton(getString(R.string.yes)) { dialog, which -> // Добавляет кнопку «Да»
                                // Действия, выполняемые при нажатии на кнопку «Да»
                                playlistViewModel.deletePlaylist()
                                findNavController().navigateUp()
                            }
                            .show()
                    }
                    "share" -> {
                        share(updatedPlaylist!!)
                    }
                }
            }
        }
        parentFragmentManager.setFragmentResultListener("edit_playlistrequest", viewLifecycleOwner) { requestKey, bundle ->
            val updatedPlaylist = bundle.getParcelable("playlist", PlaylistPresentation::class.java)
            playlistViewModel.setPlaylist(updatedPlaylist!!)
        }
    }

    private fun share(playlist: PlaylistPresentation) {
        if (playlist.tracks_count == 0) {
            Toast.makeText(requireActivity().applicationContext, R.string.no_tracks_to_share, Toast.LENGTH_LONG).show()
        } else {
            /*
            * Сообщение для получателя должно содержать простой текст со списком треков плейлиста с названием плейлиста,
            * описанием на следующей строке, количеством треков в формате «[xx] треков»,
            * где «[xx]» — количество треков на следующей строке,
            * пронумерованным списком треков плейлиста в формате: «[номер]. [имя исполнителя] - [название трека] ([продолжительность трека])».
            */
            var result = ""
            result += playlist.name + "\n"
            result += playlist.description + "\n"
            result += playlist.tracks_count.toString() + " " + getString(R.string.treka) + "\n"
            var i = 1
            for (track in playlist.tracks) {
                result += i.toString() + ". " + track.artistName!! + " - " + track.trackName!! + " (" + SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis) + ")" + "\n"
                i += 1
            }

            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, result)
                type = "text/plain"
            }
            activity?.startActivity(sendIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        playlist_name = null
        playlist_description = null
        playlist_artwork = null
        playlist_time = null
        playlist_track_count = null
        playlist_tracks?.adapter = null
        playlist_tracks = null
        playlist_share = null
        playlist_menu = null
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