package ru.chivarzin.aleksandr.playlistmaker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.PlaylistPresentation

class PlaylistGridAdapter(private val playlists: List<PlaylistPresentation>, val callback: OnPlaylistClickCallback, val treka: String = "трека"): RecyclerView.Adapter<PlaylistGridViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistGridViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_playlist_grid, parent, false)
        return PlaylistGridViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PlaylistGridViewHolder,
        position: Int
    ) {
        holder.bind(playlists[position], treka, callback)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}