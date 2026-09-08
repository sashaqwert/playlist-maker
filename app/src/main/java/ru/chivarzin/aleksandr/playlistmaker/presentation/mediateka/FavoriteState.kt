package ru.chivarzin.aleksandr.playlistmaker.presentation.mediateka

import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation

sealed interface FavoriteState {
    object Loading: FavoriteState
    data class Content(
        val tracks: List<TrackPresentation>
    ) : FavoriteState
    object Empty: FavoriteState
}