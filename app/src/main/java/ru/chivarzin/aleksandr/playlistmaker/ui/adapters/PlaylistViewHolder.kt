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

class PlaylistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val artwork = itemView.findViewById<ImageView>(R.id.artwork)
    val playlist_name = itemView.findViewById<TextView>(R.id.playlist_name)
    val tracks_count = itemView.findViewById<TextView>(R.id.tracks_count)

    fun bind(model: Playlist, callback: OnPlaylistClickCallback) {
        if (model.artwork_path != "") {
            Glide.with(itemView)
                .load(model.artwork_path.toUri())
                .centerCrop()
                .placeholder(R.drawable.artwork_default)
                .transform(RoundedCorners(dpToPx(2.0f, itemView.context)))
                .into(artwork)
        }
        else {
            Glide.with(itemView)
                .load(R.drawable.artwork_default)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(2.0f, itemView.context)))
                .into(artwork)
        }
        playlist_name.setText(model.name)
        tracks_count.setText("${model.tracks_count} ${itemView.context.getString(R.string.treka)}")
        itemView.setOnClickListener {
            callback.callback(model)
        }
    }
}