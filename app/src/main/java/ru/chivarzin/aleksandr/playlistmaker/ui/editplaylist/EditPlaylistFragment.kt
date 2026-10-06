package ru.chivarzin.aleksandr.playlistmaker.ui.editplaylist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.dpToPx
import ru.chivarzin.aleksandr.playlistmaker.presentation.editplaylist.EditPlaylistViewModel
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.PlaylistPresentation
import ru.chivarzin.aleksandr.playlistmaker.ui.newplaylist.NewPlaylistFragment
import kotlin.random.Random

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PLAYLIST = "playlist"

/**
 * A simple [Fragment] subclass.
 * Use the [EditPlaylistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditPlaylistFragment : NewPlaylistFragment() {
    private var playlist: PlaylistPresentation? = null
    private val editPlaylistViewModel: EditPlaylistViewModel by viewModel()

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
            filename = playlist!!.artwork_path
            Glide.with(this)
                .load(playlist!!.artwork_path.toUri())
                .transform(CenterCrop(), RoundedCorners(dpToPx(8.0f, requireActivity())))
                .into(edit_playlist_artwork!!)
        }
        edit_playlist_name?.setText(playlist?.name)
        edit_playlist_description?.setText(playlist?.description)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    Glide.with(this)
                        .load(uri)
                        .transform(CenterCrop(), RoundedCorners(dpToPx(8.0f, requireActivity())))
                        .into(edit_playlist_artwork!!)
                    filename = uri.toString()
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        edit_playlist_artwork?.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        save?.setOnClickListener {
            val name = Random.nextInt().toString()
            if (filename != "") {
                filename = editPlaylistViewModel.saveFile(filename.toUri(), name).toString()
            }
            if (playlist != null) {
                playlist!!.name = edit_playlist_name?.text.toString()
                playlist!!.description = edit_playlist_description!!.text!!.toString()
                playlist!!.artwork_path = filename
                editPlaylistViewModel.saveButtonClicked(playlist!!)
            }
        }
        editPlaylistViewModel.obsorveSave().observe(viewLifecycleOwner) {
            if (it) {
                val result = Bundle().apply {
                    putString("action", "edited")
                    putParcelable("playlist", playlist)
                }
                setFragmentResult("edit_playlistrequest", result)
                findNavController().navigateUp()
            }
        }
        if (savedInstanceState != null) {
            filename = savedInstanceState.getString("artwork", "")
            if (filename != "") {
                Glide.with(this)
                    .load(filename.toUri())
                    .transform(CenterCrop(), RoundedCorners(dpToPx(8.0f, requireActivity())))
                    .into(edit_playlist_artwork!!)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        edit_playlist_action_back = null
        save = null
        edit_playlist_name = null
        edit_playlist_description = null
        edit_playlist_artwork = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("artwork", filename)
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