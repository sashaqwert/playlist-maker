package ru.chivarzin.aleksandr.playlistmaker.domain.models

data class Playlist(val id: Long = 0L,
                    val name: String,
                    val description: String,
                    val artwork_path: String,
                    val tracks: MutableList<Track> = mutableListOf<Track>(),
                    var tracks_count : Int = 0)
{
    fun add_track(track: Track): Boolean {
        if (track !in tracks) {
            tracks.add(0, track)
            tracks_count += 1
            return true
        }
        return false
    }
}

