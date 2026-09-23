package ru.chivarzin.aleksandr.playlistmaker.ui.newplaylist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation

private const val ARG_TRACK = "track"

/**
 * A simple [Fragment] subclass.
 * Use the [NewPlaylistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewPlaylistFragment : Fragment() {
    private var track: TrackPresentation? = null

    var new_playlist_action_back: ImageView? = null
    var create: AppCompatButton? = null
    var newplaylist_name: TextInputEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            track = it.getParcelable(ARG_TRACK, TrackPresentation::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        new_playlist_action_back = view.findViewById<ImageView>(R.id.new_playlist_action_back)
        new_playlist_action_back?.setOnClickListener {
            findNavController().navigateUp()
        }
        create = view.findViewById<AppCompatButton>(R.id.create)
        newplaylist_name = view.findViewById<TextInputEditText>(R.id.newplaylist_name)
        newplaylist_name?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val str = s.toString()
                create?.isEnabled = str != ""
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        new_playlist_action_back = null
        create = null
        newplaylist_name = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param track Parameter 1.
         * @return A new instance of fragment NewPlaylistFragment.
         */
        @JvmStatic
        fun newInstance(track: TrackPresentation?) =
            NewPlaylistFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_TRACK, track)
                }
            }

        fun createArgs(track: TrackPresentation): Bundle =
            bundleOf(ARG_TRACK to track)
    }
}