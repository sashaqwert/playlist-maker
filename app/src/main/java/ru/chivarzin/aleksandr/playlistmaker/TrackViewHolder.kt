package ru.chivarzin.aleksandr.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
    val artwork = itemView.findViewById<ImageView>(R.id.artwork)
    val track_name = itemView.findViewById<TextView>(R.id.track_name)
    val artist_name = itemView.findViewById<TextView>(R.id.artist_name)
    val track_time = itemView.findViewById<TextView>(R.id.track_time)

    fun bind(model: Track) {
        track_name.setText(model.trackName)
        artist_name.setText(model.artistName)
        track_time.setText(model.trackTime)

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .fitCenter()
            .placeholder(R.drawable.artwork_default)
            .transform(RoundedCorners(10))
            .into(artwork)
    }
}