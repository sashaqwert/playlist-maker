package ru.chivarzin.aleksandr.playlistmaker

object SearchHistory {
    const val MAX_COUNT = 10
    var history: ArrayList<Track> = ArrayList()

    fun add(track: Track) {
        for (i in history.indices) {
            if (history.get(i).trackId == track.trackId) {
                history.removeAt(i)
            }
        }
        history.add(0, track)
        if (history.size > MAX_COUNT) {
            history.removeAt(MAX_COUNT)
        }
    }

    fun clear() = history.clear()

    fun isEmpty() = history.isEmpty()
}