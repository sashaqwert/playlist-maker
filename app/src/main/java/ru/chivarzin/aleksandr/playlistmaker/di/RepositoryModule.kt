package ru.chivarzin.aleksandr.playlistmaker.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.chivarzin.aleksandr.playlistmaker.data.network.TracksRepositoryImpl
import ru.chivarzin.aleksandr.playlistmaker.data.storage.SearchHistoryRepositoryImpl
import ru.chivarzin.aleksandr.playlistmaker.data.storage.ThemeRepositoryImpl
import ru.chivarzin.aleksandr.playlistmaker.domain.api.SearchHistoryRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.api.ThemeRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.api.TracksRepository

val repositoryModule = module {
    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }

    single<ThemeRepository>{
        ThemeRepositoryImpl(androidContext())
    }
}