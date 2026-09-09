package ru.chivarzin.aleksandr.playlistmaker.ui.mediateka

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.presentation.mediateka.FavoriteState
import ru.chivarzin.aleksandr.playlistmaker.presentation.mediateka.FavoriteViewModel
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation
import ru.chivarzin.aleksandr.playlistmaker.ui.player.PlayerFragment

class FavoriteFragment : Fragment() {
    private val favoriteViewModel: FavoriteViewModel by viewModel()
    private var favorite_tracks: RecyclerView? = null
    private var favorite_pb: ProgressBar? = null
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
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favorite_tracks = view.findViewById<RecyclerView>(R.id.favorite_tracks)
        favorite_pb = view.findViewById<ProgressBar>(R.id.favorite_pb)
        icon_error = view.findViewById<ImageView>(R.id.icon_error)
        error_text = view.findViewById<TextView>(R.id.error_text)

        favoriteViewModel.fillData()
        favoriteViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        favorite_tracks?.adapter = null
        favorite_tracks = null
        favorite_pb = null
        icon_error = null
        error_text = null
    }

    private fun showLoading() {
        favorite_tracks?.visibility = View.GONE
        favorite_pb?.visibility = View.VISIBLE
        icon_error?.visibility = View.GONE
        error_text?.visibility = View.GONE
    }

    private fun showEmpty() {
        favorite_tracks?.visibility = View.GONE
        favorite_pb?.visibility = View.GONE
        icon_error?.visibility = View.VISIBLE
        error_text?.visibility = View.VISIBLE
    }

    private fun showContent(tracks: List<TrackPresentation>) {
        favorite_tracks?.visibility = View.VISIBLE
        favorite_pb?.visibility = View.GONE
        icon_error?.visibility = View.GONE
        error_text?.visibility = View.GONE

        val adapter = TrackAdapter(ArrayList<TrackPresentation>(tracks), object : OnItemClickCallback {
            override fun callback(track: TrackPresentation) {
                findNavController().navigate(
                    R.id.action_mediatekaFragment_to_playerFragment,
                    PlayerFragment.createArgs(track))
            }
        })
        favorite_tracks?.adapter = adapter
    }

    private fun render(state: FavoriteState) {
        when(state) {
            is FavoriteState.Loading -> showLoading()
            is FavoriteState.Empty -> showEmpty()
            is FavoriteState.Content -> showContent(state.tracks)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}