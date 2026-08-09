package ru.chivarzin.aleksandr.playlistmaker.ui.player

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.dpToPx
import ru.chivarzin.aleksandr.playlistmaker.isDarkTheme
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation
import ru.chivarzin.aleksandr.playlistmaker.presentation.player.PlayerState
import ru.chivarzin.aleksandr.playlistmaker.presentation.player.PlayerViewModel
import java.util.Locale
import kotlin.getValue

private const val ARG_TRACK = "track"

/**
 * A simple [Fragment] subclass.
 * Use the [PlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayerFragment : Fragment() {
    private val playerViewModel: PlayerViewModel by viewModel {
        parametersOf(track)
    }
    private lateinit var track: TrackPresentation
    private var player_playpause: ImageView? = null
    private var player_progress: TextView? = null

    private var player_artwork: ImageView? = null
    private var player_track_name: TextView? = null
    private var player_artist_name: TextView? = null
    private var player_duration: TextView? = null
    private var player_collection_hint: TextView? = null
    private var player_collection_name: TextView? = null
    private var player_release_date_hint: TextView? = null
    private var player_release_date: TextView? = null
    private var player_janr: TextView? = null
    private var player_country: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            track = it.getParcelable(ARG_TRACK, TrackPresentation::class.java)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val player_action_back = view.findViewById<ImageView>(R.id.player_action_back)
        player_action_back.setOnClickListener {
            findNavController().navigateUp()
        }

        player_artwork = view.findViewById<ImageView>(R.id.player_artwork)
        player_track_name = view.findViewById<TextView>(R.id.player_track_name)
        player_artist_name = view.findViewById<TextView>(R.id.player_artist_name)
        player_duration = view.findViewById<TextView>(R.id.player_duration)
        player_collection_hint = view.findViewById<TextView>(R.id.player_collection_hint)
        player_collection_name = view.findViewById<TextView>(R.id.player_collection_name)
        player_release_date_hint = view.findViewById<TextView>(R.id.player_release_date_hint)
        player_release_date = view.findViewById<TextView>(R.id.player_release_date)
        player_janr = view.findViewById<TextView>(R.id.player_janr)
        player_country = view.findViewById<TextView>(R.id.player_country)
        player_playpause = view.findViewById<ImageView>(R.id.player_playpause)
        player_progress = view.findViewById<TextView>(R.id.player_progress)

        playerViewModel.observeUiState().observe(viewLifecycleOwner) {
            render(it)
        }
        player_playpause?.setOnClickListener {
            playerViewModel.onPlayButtonClicked()
        }
    }

    private fun initialize(track: TrackPresentation) {
        if (track.artworkUrl100 != null && player_artwork != null) {
            Glide.with(this)
                .load(track.getCoverArtwork())
                .fitCenter()
                .transform(RoundedCorners(dpToPx(8.0f, requireActivity())))
                .placeholder(R.drawable.artwork_default)
                .into(player_artwork!!)
        }
        if (track.trackName != null) {
            player_track_name?.setText(track.trackName)
        }
        if (track.artistName != null) {
            player_artist_name?.setText(track.artistName)
        }
        if (track.trackTimeMillis != null) {
            player_duration?.setText(SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis))
        }
        if (track.collectionName == null) {
            player_collection_hint?.visibility = View.GONE
            player_collection_name?.visibility = View.GONE
        } else {
            player_collection_name?.setText(track.collectionName)
        }
        if (track.releaseDate == null) {
            player_release_date_hint?.visibility = View.GONE
            player_release_date?.visibility = View.GONE
        } else {
            player_release_date?.setText(track.getYear()!!.toString())
        }
        if (track.primaryGenreName != null) {
            player_janr?.setText(track.primaryGenreName)
        }
        if (track.country != null) {
            player_country?.setText(track.country)
        }
    }

    private fun player_state_changed(state: Int) {
        when (state) {
            // Оставлено для понимания.
            // Если раскомментировать, будет сломка макета
//            PlayerViewModel.STATE_DEFAULT -> if (isDarkTheme(this)) {
//                Glide.with(this)
//                    .load(R.drawable.play_dark)
//                    .fitCenter()
//                    .into(player_playpause)
//            } else {
//                Glide.with(this)
//                    .load(R.drawable.play)
//                    // .fitCenter()
//                    .into(player_playpause)
//            }

            PlayerViewModel.STATE_PREPARED -> if (isDarkTheme(requireActivity())) {
                Glide.with(this)
                    .load(R.drawable.play_dark)
                    .fitCenter()
                    .into(player_playpause!!)
            } else {
                Glide.with(this)
                    .load(R.drawable.play)
                    .fitCenter()
                    .into(player_playpause!!)
            }

            PlayerViewModel.STATE_PLAYING -> if (isDarkTheme(requireActivity())) {
                Glide.with(this)
                    .load(R.drawable.pause_dark)
                    .fitCenter()
                    .into(player_playpause!!)
            } else {
                Glide.with(this)
                    .load(R.drawable.pause)
                    .fitCenter()
                    .into(player_playpause!!)
            }

            PlayerViewModel.STATE_PAUSED -> if (isDarkTheme(requireActivity())) {
                Glide.with(this)
                    .load(R.drawable.play_dark)
                    //  .fitCenter()
                    .into(player_playpause!!)
            } else {
                Glide.with(this)
                    .load(R.drawable.play)
                    .fitCenter()
                    .into(player_playpause!!)
            }
        }
    }

    private fun update_progress(progress: String) {
        player_progress?.text = progress
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.onPause()
    }

    fun render (state: PlayerState) {
        when(state) {
            is PlayerState.Initial -> initialize(state.track)
            is PlayerState.State -> player_state_changed(state.player_state)
            is PlayerState.Progress -> update_progress(state.progress)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        player_playpause = null
        player_progress = null

        player_artwork = null
        player_track_name = null
        player_artist_name = null
        player_duration = null
        player_collection_hint = null
        player_collection_name = null
        player_release_date_hint = null
        player_release_date = null
        player_janr = null
        player_country = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param track TRackPresentation instance.
         * @return A new instance of fragment PlayerFragment.
         */
        @JvmStatic
        fun newInstance(track: TrackPresentation) =
            PlayerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_TRACK, track)
                }
            }

        fun createArgs(track: TrackPresentation): Bundle =
            bundleOf(ARG_TRACK to track)
    }
}