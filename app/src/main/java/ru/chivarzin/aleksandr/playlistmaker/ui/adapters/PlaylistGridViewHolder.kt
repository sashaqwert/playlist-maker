package ru.chivarzin.aleksandr.playlistmaker.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist
import ru.chivarzin.aleksandr.playlistmaker.dpToPx

class PlaylistGridViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val playlist_artwork = itemView.findViewById<ImageView>(R.id.playlist_artwork)
    val playlist_name = itemView.findViewById<TextView>(R.id.playlist_name)

    fun bind(model: Playlist, treka: String) {
        playlist_name.setText("${model.name}\n${model.tracks_count} ${treka}")
        if (model.artwork_path != "") {
            Glide.with(itemView)
                .load(model.artwork_path.toUri())
                .centerCrop()
                .transform(RoundedCorners(dpToPx(8.0f, itemView.context)))
                .into(playlist_artwork)
        } else {
            Glide.with(itemView)
                .load(R.drawable.artwork_default)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(8.0f, itemView.context)))
                .into(playlist_artwork)
        }
    }
}