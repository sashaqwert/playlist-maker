package ru.chivarzin.aleksandr.playlistmaker.ui.mediateka

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation

class TrackAdapter (private val tracks: ArrayList<TrackPresentation>, val callback: OnItemClickCallback) : RecyclerView.Adapter<TrackViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], callback)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
