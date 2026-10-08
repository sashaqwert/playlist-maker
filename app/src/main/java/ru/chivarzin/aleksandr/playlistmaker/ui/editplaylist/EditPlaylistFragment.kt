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
import android.widget.TextView
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            playlist = it.getParcelable(ARG_PLAYLIST, PlaylistPresentation::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backCallback?.isEnabled = false

        val title = view.findViewById<TextView>(R.id.title_new_playlist)
        title.setText(R.string.edit)
        new_playlist_action_back?.setOnClickListener {
            findNavController().navigateUp()
        }

        if (playlist?.artwork_path != "") {
            filename = playlist!!.artwork_path
            Glide.with(this)
                .load(playlist!!.artwork_path.toUri())
                .transform(CenterCrop(), RoundedCorners(dpToPx(8.0f, requireActivity())))
                .into(newplaylist_artwork!!)
        }
        newplaylist_name?.setText(playlist?.name)
        newplaylist_description?.setText(playlist?.description)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    Glide.with(this)
                        .load(uri)
                        .transform(CenterCrop(), RoundedCorners(dpToPx(8.0f, requireActivity())))
                        .into(newplaylist_artwork!!)
                    filename = uri.toString()
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        newplaylist_artwork?.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        create?.setText(R.string.save_playlist_button_text)
        create?.setOnClickListener {
            if (filename != playlist!!.artwork_path) {
                val name = Random.nextInt().toString()
                if (filename != "") {
                    filename = editPlaylistViewModel.saveFile(filename.toUri(), name).toString()
                }
            }
            if (playlist != null) {
                playlist!!.name = newplaylist_name?.text.toString()
                playlist!!.description = newplaylist_description!!.text!!.toString()
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
            filename = savedInstanceState.getString("artwork_", "")
            if (filename != "") {
                Glide.with(this)
                    .load(filename.toUri())
                    .transform(CenterCrop(), RoundedCorners(dpToPx(8.0f, requireActivity())))
                    .into(newplaylist_artwork!!)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("artwork_", filename)
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