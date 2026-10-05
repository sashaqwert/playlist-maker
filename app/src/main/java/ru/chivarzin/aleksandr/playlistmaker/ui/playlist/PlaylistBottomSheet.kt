package ru.chivarzin.aleksandr.playlistmaker.ui.playlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.dpToPx
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.PlaylistPresentation

class PlaylistBottomSheet : BottomSheetDialogFragment() {
    private var playlist: PlaylistPresentation? = null
    private var listener: PlaylistBottomSheetListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val playlist = it.getParcelable("arg_playlist", PlaylistPresentation::class.java)
            this.playlist = playlist
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Получаем ссылку на родительский фрагмент
        val parentFragment = parentFragment
        if (parentFragment is PlaylistBottomSheetListener) {
            listener = parentFragment
        } else {
            throw ClassCastException("$parentFragment must implement PlaylistBottomSheetListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val artwork = view.findViewById<ImageView>(R.id.artwork)
        val playlist_name = view.findViewById<TextView>(R.id.playlist_name)
        val tracks_count = view.findViewById<TextView>(R.id.tracks_count)

        if (playlist?.artwork_path != "") {
            Glide.with(this)
                .load(playlist?.artwork_path?.toUri())
                .placeholder(R.drawable.artwork_default)
                .transform(CenterCrop(), RoundedCorners(dpToPx(2.0f, requireActivity())))
                .into(artwork)
        }
        else {
            Glide.with(view)
                .load(R.drawable.artwork_default)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(2.0f, requireActivity())))
                .into(artwork)
        }
        playlist_name.setText(playlist?.name)
        tracks_count.setText("${playlist?.tracks_count} ${getString(R.string.treka)}")

        // Пример для кнопки "Редактировать"
        view.findViewById<View>(R.id.action_edit).setOnClickListener {
            listener?.onPlaylistAction("edit", playlist)
            dismiss() // Закрываем BottomSheet после действия
        }

        // Пример для кнопки "Удалить"
        view.findViewById<View>(R.id.action_delete).setOnClickListener {
            listener?.onPlaylistAction("delete", playlist)
            dismiss()
        }

        // Пример для кнопки "Поделиться"
        view.findViewById<View>(R.id.action_share).setOnClickListener {
            listener?.onPlaylistAction("share", playlist)
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null // Избегаем утечки памяти
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