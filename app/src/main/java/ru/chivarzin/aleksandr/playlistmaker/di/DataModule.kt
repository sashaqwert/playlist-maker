package ru.chivarzin.aleksandr.playlistmaker.di

import android.content.Context
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.chivarzin.aleksandr.playlistmaker.APP_PREFERENCES
import ru.chivarzin.aleksandr.playlistmaker.data.NetworkClient
import ru.chivarzin.aleksandr.playlistmaker.data.SearchHistoryDataSource
import ru.chivarzin.aleksandr.playlistmaker.data.ThemeDataSource
import ru.chivarzin.aleksandr.playlistmaker.data.network.ITunesApi
import ru.chivarzin.aleksandr.playlistmaker.data.network.RetrofitNetworkClient
import ru.chivarzin.aleksandr.playlistmaker.data.storage.SearchHistoryRepositoryImpl
import ru.chivarzin.aleksandr.playlistmaker.data.storage.ThemeRepositoryImpl
import ru.chivarzin.aleksandr.playlistmaker.domain.api.SearchHistoryRepository
import ru.chivarzin.aleksandr.playlistmaker.domain.api.ThemeRepository

val dataModule = module {

    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<SearchHistoryDataSource> {
        SearchHistoryDataSource(get())
    }

    single<ThemeDataSource> {
        ThemeDataSource(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }

    single<ThemeRepository>{
        ThemeRepositoryImpl(get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }
}