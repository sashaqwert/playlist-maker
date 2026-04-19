package ru.chivarzin.aleksandr.playlistmaker

class SearchHistory {
    var history: ArrayList<Track> = ArrayList()

    fun add(track: Track) {
        for (i in history.indices) {
            if (history.get(i).trackId == track.trackId) {
                history.removeAt(i)
            }
        }
        history.add(0, track)
        if (history.size > MAX_COUNT) {
            history.removeAt(MAX_COUNT - 1)
        }
    }

    fun clear() = history.clear()

    companion object {
        const val MAX_COUNT = 10
    }
}