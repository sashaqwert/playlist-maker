package ru.chivarzin.aleksandr.playlistmaker.domain.api

interface ThemeInteractor {
    fun saveTheme(isDarkTheme: Boolean)
    fun getTheme() : Boolean
}