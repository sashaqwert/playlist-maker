package ru.chivarzin.aleksandr.playlistmaker

import ru.chivarzin.aleksandr.playlistmaker.data.network.RetrofitNetworkClient
import ru.chivarzin.aleksandr.playlistmaker.data.network.TracksRepositoryImpl
import ru.chivarzin.aleksandr.playlistmaker.domain.api.TracksInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.api.TracksRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}