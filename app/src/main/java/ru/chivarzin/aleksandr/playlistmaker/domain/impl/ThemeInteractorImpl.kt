package ru.chivarzin.aleksandr.playlistmaker.domain.impl

import ru.chivarzin.aleksandr.playlistmaker.domain.api.ThemeInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.api.ThemeRepository

class ThemeInteractorImpl (private val repository: ThemeRepository) : ThemeInteractor {
    override fun saveTheme(isDarkTheme: Boolean) {
        repository.saveTheme(isDarkTheme)
    }

    override fun getTheme(): Boolean {
        return repository.getTheme()
    }
}