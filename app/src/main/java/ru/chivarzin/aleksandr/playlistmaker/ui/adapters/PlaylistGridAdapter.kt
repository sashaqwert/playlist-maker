package ru.chivarzin.aleksandr.playlistmaker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist

class PlaylistGridAdapter(private val playlists: List<Playlist>, val treka: String = "трека"): RecyclerView.Adapter<PlaylistGridViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistGridViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_playlist, parent, false)
        return PlaylistGridViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PlaylistGridViewHolder,
        position: Int
    ) {
        holder.bind(playlists[position], treka)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}