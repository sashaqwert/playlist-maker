package ru.chivarzin.aleksandr.playlistmaker.di

import android.media.MediaPlayer
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track
import ru.chivarzin.aleksandr.playlistmaker.presentation.mediateka.FavoriteViewModel
import ru.chivarzin.aleksandr.playlistmaker.presentation.mediateka.PlaylistsViewModel
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation
import ru.chivarzin.aleksandr.playlistmaker.presentation.player.PlayerViewModel
import ru.chivarzin.aleksandr.playlistmaker.presentation.search.SearchViewModel
import ru.chivarzin.aleksandr.playlistmaker.presentation.settings.SettingsViewModel

val viewModelModule = module {

    factory {
        MediaPlayer()
    }

    viewModel {
        SearchViewModel(get(), get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel { (track: TrackPresentation) ->
        PlayerViewModel(track, get())
    }

    viewModel {
        FavoriteViewModel()
    }

    viewModel {
        PlaylistsViewModel()
    }
}