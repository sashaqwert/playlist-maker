package ru.chivarzin.aleksandr.playlistmaker.presentation.search

import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation

sealed interface SearchState {
    object Loading : SearchState

    data class History (
        val tracks: List<TrackPresentation>
    ) : SearchState

    object emptyHistory : SearchState

    data class Content(
        val tracks: List<TrackPresentation>
    ) : SearchState

    data class Error(
        val message: String
    ) : SearchState

    data class Empty(
        val message: String
    ) : SearchState
}