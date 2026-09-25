package ru.chivarzin.aleksandr.playlistmaker.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist

class PlaylistGridViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val playlist_artwork = itemView.findViewById<ImageView>(R.id.playlist_artwork)
    val playlist_name = itemView.findViewById<TextView>(R.id.playlist_name)

    fun bind(model: Playlist, treka: String) {
        playlist_name.setText("${model.name}\n${model.tracks_count} ${treka}")
        if (model.artwork_path != "") {
            playlist_artwork.setImageURI(model.artwork_path.toUri())
        }
    }
}