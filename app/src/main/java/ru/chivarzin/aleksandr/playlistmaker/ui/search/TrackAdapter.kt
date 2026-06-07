package ru.chivarzin.aleksandr.playlistmaker.ui.search

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

class TrackAdapter (private val tracks: ArrayList<Track>, val activity: SearchActivity? = null) : RecyclerView.Adapter<TrackViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], clickDebounce, activity = activity)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private var clickDebounce : (() -> Boolean) = {
        val current: Boolean = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}