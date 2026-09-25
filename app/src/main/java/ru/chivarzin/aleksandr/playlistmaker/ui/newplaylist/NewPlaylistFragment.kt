package ru.chivarzin.aleksandr.playlistmaker.ui.newplaylist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation
import ru.chivarzin.aleksandr.playlistmaker.presentation.newplaylist.NewPlaylistViewModel
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

private const val ARG_TRACK = "track"

/**
 * A simple [Fragment] subclass.
 * Use the [NewPlaylistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewPlaylistFragment : Fragment() {
    private var track: TrackPresentation? = null
    private val newPlaylistViewModel: NewPlaylistViewModel by viewModel {
        parametersOf(track) //Как здесь обработать не NULL случай?
    }

    var new_playlist_action_back: ImageView? = null
    var create: AppCompatButton? = null
    var newplaylist_name: TextInputEditText? = null
    var newplaylist_description: TextInputEditText? = null
    var newplaylist_artwork: ImageView? = null

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
        newplaylist_description = view.findViewById<TextInputEditText>(R.id.newplaylist_description)
        newplaylist_artwork = view.findViewById<ImageView>(R.id.newplaylist_artwork)
        var filename = ""
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    newplaylist_artwork?.setImageURI(uri)
                    val name = Random.nextInt().toString()
                    filename = saveImageToPrivateStorage(uri, name).toString()
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        //по нажатию на кнопку pickImage запускаем photo picker
        newplaylist_artwork?.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        create?.setOnClickListener {
            newPlaylistViewModel.createButtonClicked(newplaylist_name?.text?.toString()!!, newplaylist_description?.text?.toString()!!, filename)
        }
        newPlaylistViewModel.obsorveSave().observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireActivity().applicationContext, "${getString(R.string.playlist)} ${newplaylist_name?.text.toString()} ${getString(R.string.created)}", Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        new_playlist_action_back = null
        create = null
        newplaylist_name = null
        newplaylist_description = null
        newplaylist_artwork = null
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(getString(R.string.dialog_title)) // Заголовок диалога
            .setMessage(getString(R.string.dialog_content)) // Описание диалога
            .setNeutralButton(activity?.getString(R.string.cancel)) { dialog, which -> // Добавляет кнопку «Отмена»
                // Действия, выполняемые при нажатии на кнопку «Отмена»
            }
            //.setNegativeButton("Нет") { dialog, which -> // Добавляет кнопку «Нет»
                // Действия, выполняемые при нажатии на кнопку «Нет»
            //}
            .setPositiveButton(getString(R.string.compate)) { dialog, which -> // Добавляет кнопку «Да»
                // Действия, выполняемые при нажатии на кнопку «Да»
            }
            .show()
    }

    private fun saveImageToPrivateStorage(uri: Uri, filename_without_extension: String): Uri {
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        //создаем каталог, если он не создан
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, "${filename_without_extension}.jpg")
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = activity?.contentResolver?.openInputStream(uri)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.toUri()
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