package ru.chivarzin.aleksandr.playlistmaker

import android.content.Context
import ru.chivarzin.aleksandr.playlistmaker.data.network.RetrofitNetworkClient
import ru.chivarzin.aleksandr.playlistmaker.data.network.TracksRepositoryImpl
import ru.chivarzin.aleksandr.playlistmaker.data.storage.SearchHistoryRepositoryImpl
import ru.chivarzin.aleksandr.playlistmaker.domain.api.SearchHistoryInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.api.SearchHistoryRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.api.TracksInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.api.TracksRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import ru.chivarzin.aleksandr.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getSearchHistoryRepository(context: Context) : SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(context)
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideSearchHistoryInteractor(context: Context) : SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }
}