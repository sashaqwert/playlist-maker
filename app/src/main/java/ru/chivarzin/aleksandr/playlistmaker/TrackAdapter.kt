package ru.chivarzin.aleksandr.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter (private val tracks: ArrayList<Track>, val activity: SearchActivity? = null) : RecyclerView.Adapter<TrackViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], activity = activity)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}