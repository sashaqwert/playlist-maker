package ru.chivarzin.aleksandr.playlistmaker.ui.editplaylist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.textfield.TextInputEditText
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.dpToPx
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.PlaylistPresentation

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PLAYLIST = "playlist"

/**
 * A simple [Fragment] subclass.
 * Use the [EditPlaylistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditPlaylistFragment : Fragment() {
    private var playlist: PlaylistPresentation? = null

    private var edit_playlist_action_back: ImageView? = null
    private var save: AppCompatButton? = null
    private var edit_playlist_name: TextInputEditText? = null
    private var edit_playlist_description: TextInputEditText? = null
    private var edit_playlist_artwork: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            playlist = it.getParcelable(ARG_PLAYLIST, PlaylistPresentation::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edit_playlist_action_back = view.findViewById<ImageView>(R.id.edit_playlist_action_back)
        edit_playlist_action_back?.setOnClickListener {
            findNavController().navigateUp()
        }
        save = view.findViewById<AppCompatButton>(R.id.save)
        edit_playlist_name = view.findViewById<TextInputEditText>(R.id.edit_playlist_name)
        edit_playlist_description = view.findViewById<TextInputEditText>(R.id.edit_playlist_description)
        edit_playlist_name?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val str = s.toString()
                save?.isEnabled = str != "" && !str.isBlank()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        edit_playlist_artwork = view.findViewById<ImageView>(R.id.edit_playlist_artwork)
        if (playlist?.artwork_path != "") {
            Glide.with(this)
                .load(playlist!!.artwork_path.toUri())
                .transform(CenterCrop(), RoundedCorners(dpToPx(8.0f, requireActivity())))
                .into(edit_playlist_artwork!!)
        }
        edit_playlist_name?.setText(playlist?.name)
        edit_playlist_description?.setText(playlist?.description)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        edit_playlist_action_back = null
        save = null
        edit_playlist_name = null
        edit_playlist_description = null
        edit_playlist_artwork = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param playlist Parameter 1.
         * @return A new instance of fragment EditPlaylistFragment.
         */
        @JvmStatic
        fun newInstance(playlist: PlaylistPresentation) =
            EditPlaylistFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PLAYLIST, playlist)
                }
            }

        fun createArgs(playlist: PlaylistPresentation): Bundle =
            bundleOf(ARG_PLAYLIST to playlist)
    }
}