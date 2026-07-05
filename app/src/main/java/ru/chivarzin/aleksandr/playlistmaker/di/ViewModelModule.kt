package ru.chivarzin.aleksandr.playlistmaker.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.chivarzin.aleksandr.playlistmaker.presentation.search.SearchViewModel
import ru.chivarzin.aleksandr.playlistmaker.presentation.settings.SettingsViewModel

val viewModelModule = module {

    viewModel {
        SearchViewModel(get(), get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }
}