package ru.chivarzin.aleksandr.playlistmaker.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.chivarzin.aleksandr.playlistmaker.data.converters.TrackDbConverter
import ru.chivarzin.aleksandr.playlistmaker.data.db.FavoriteRepositoryImpl
import ru.chivarzin.aleksandr.playlistmaker.data.network.TracksRepositoryImpl
import ru.chivarzin.aleksandr.playlistmaker.data.storage.SearchHistoryRepositoryImpl
import ru.chivarzin.aleksandr.playlistmaker.data.storage.ThemeRepositoryImpl
import ru.chivarzin.aleksandr.playlistmaker.domain.api.SearchHistoryRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.api.ThemeRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.api.TracksRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.db.FavoriteRepository

val repositoryModule = module {
    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }

    single<ThemeRepository>{
        ThemeRepositoryImpl(androidContext())
    }

    single {
        TrackDbConverter()
    }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }
}