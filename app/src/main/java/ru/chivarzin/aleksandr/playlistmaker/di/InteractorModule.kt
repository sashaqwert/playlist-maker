package ru.chivarzin.aleksandr.playlistmaker.di

import org.koin.dsl.module
import ru.chivarzin.aleksandr.playlistmaker.domain.api.SearchHistoryInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.api.ThemeInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.api.TracksInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import ru.chivarzin.aleksandr.playlistmaker.domain.impl.ThemeInteractorImpl
import ru.chivarzin.aleksandr.playlistmaker.domain.impl.TracksInteractorImpl

val interactorModule = module {

    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    single<ThemeInteractor> {
        ThemeInteractorImpl(get())
    }
}