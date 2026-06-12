package ru.chivarzin.aleksandr.playlistmaker.domain.api

interface ThemeRepository {
    fun saveTheme(isDarkTheme: Boolean)
    fun getTheme() : Boolean
}