package ru.chivarzin.aleksandr.playlistmaker.presentation.models

import ru.chivarzin.aleksandr.playlistmaker.domain.models.Playlist
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track

data class PlaylistPresentation(val id: Long = 0L,
                                val name: String,
                                val description: String,
                                val artwork_path: String,
                                val tracks: MutableList<TrackPresentation> = mutableListOf<TrackPresentation>(),
                                var tracks_count : Int = 0)
{
    constructor(playlist: Playlist): this(playlist.id,
        playlist.name,
        playlist.description,
        playlist.artwork_path,
        playlist.tracks.map { TrackPresentation(it) } as MutableList<TrackPresentation>,
        playlist.tracks_count)

    fun add_track(track: TrackPresentation): Boolean {
        if (track !in tracks) {
            tracks.add(track)
            tracks_count += 1
            return true
        }
        return false
    }
}